package com.example.twinklesns.repository.SnsPost;

import com.example.twinklesns.domain.entity.QFileEntity;
import com.example.twinklesns.domain.entity.QSnsPostEntity;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import static com.example.twinklesns.domain.entity.QFileEntity.fileEntity;
import static com.example.twinklesns.domain.entity.QSnsPostEntity.snsPostEntity;
import static com.example.twinklesns.domain.entity.QCommentEntity.commentEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomSnsPostRepositoryImpl implements CustomSnsPostRepository {

    private final JPAQueryFactory jpaQueryFactory;


    /*
    @Override
    public Page<SnsPostResponseDto> DynamicFindAll(Pageable pageable, Map<String, String> searchCondition) {
        log.info("page select start");

        List<SnsPostEntity> result = jpaQueryFactory.selectFrom(snsPostEntity)
                .leftJoin(snsPostEntity.files,fileEntity)
                .fetchJoin()
                .where(condition(searchCondition))
                .orderBy(snsPostEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SnsPostResponseDto> content = result.stream()
                .map(post ->SnsPostResponseDto.builder().snsPostEntity(post).build()).collect(Collectors.toList());

        log.info("page select fin");
        JPAQuery<SnsPostEntity> totalCount = jpaQueryFactory.select(snsPostEntity)
                .from(snsPostEntity)
                .leftJoin(snsPostEntity.files, fileEntity).fetchJoin()
                .where(condition(searchCondition));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchCount);

    }
*/
    @Override
    public Page<SnsPostResponseDto> DynamicFindAll(Pageable pageable, Map<String, String> searchCondition) {
        log.info("page select start");

        // 게시글과 댓글 개수를 함께 조회하는 쿼리
        List<Tuple> result = jpaQueryFactory
                .select(
                        snsPostEntity,
                        JPAExpressions.select(commentEntity.count())   // 댓글 개수를 서브쿼리로 조회
                                .from(commentEntity)
                                .where(commentEntity.postId.eq(snsPostEntity.id))
                )
                .from(snsPostEntity)
                .leftJoin(snsPostEntity.files, fileEntity).fetchJoin()  // 파일을 fetch join으로 가져옴
                .where(condition(searchCondition))  // 검색 조건을 처리
                .groupBy(snsPostEntity.id)          // group by로 N+1 문제 방지
                .orderBy(snsPostEntity.id.desc())   // 게시물 ID로 내림차순 정렬
                .offset(pageable.getOffset())       // 페이징 처리
                .limit(pageable.getPageSize())
                .fetch();

        // 결과를 DTO로 변환
        List<SnsPostResponseDto> content = result.stream()
                .map(tuple -> SnsPostResponseDto.builder()
                        .snsPostEntity(tuple.get(snsPostEntity))    // 게시글 엔티티
                        .commentCount(tuple.get(1, Long.class))     // 댓글 개수
                        .build())
                .collect(Collectors.toList());

        log.info("page select fin");

        // 전체 게시글 수를 조회하는 쿼리
        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(snsPostEntity.countDistinct())
                .from(snsPostEntity)
                .leftJoin(snsPostEntity.files, fileEntity)  // 파일도 함께 조회
                .where(condition(searchCondition));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    /*
   @Override
public Page<SnsPostResponseDto> DynamicFindAll(Pageable pageable, Map<String, String> searchCondition) {
    log.info("page select start");

    NumberPath<Long> commentCount = Expressions.numberPath(Long.class, "commentCount");

    // selectDistinct로 중복된 데이터를 제거합니다.
       List<Tuple> result = jpaQueryFactory
               .select(snsPostEntity,
                       JPAExpressions.select(commentEntity.count())
                               .from(commentEntity)
                               .where(commentEntity.snsPost.eq(snsPostEntity)))
               .from(snsPostEntity)
               .leftJoin(snsPostEntity.files, fileEntity).fetchJoin()  // fileEntity를 fetch join으로 가져옴
               .leftJoin(snsPostEntity.comments, commentEntity)        // comment는 개수만 셈
               .where(condition(searchCondition))
               .groupBy(snsPostEntity.id)
               .orderBy(snsPostEntity.id.desc())
               .offset(pageable.getOffset())
               .limit(pageable.getPageSize())
               .fetch();

    List<SnsPostResponseDto> content = result.stream()
            .map(tuple -> SnsPostResponseDto.builder()
                    .snsPostEntity(tuple.get(snsPostEntity))
                    .commentCount(tuple.get(1, Long.class))
                    .build())
            .collect(Collectors.toList());

    log.info("page select fin");

    JPAQuery<Long> totalCount = jpaQueryFactory.select(snsPostEntity.countDistinct())
               .from(snsPostEntity)
               .leftJoin(snsPostEntity.files, fileEntity)  // 파일도 함께 조회
               .where(condition(searchCondition));

    return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchCount);
}
*/


    private BooleanBuilder condition(Map<String, String> searchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(categoryEq(searchCondition.getOrDefault("category", null)));
        booleanBuilder.and(memberIdEq(searchCondition.getOrDefault("memberId", null)));
        return booleanBuilder;
    }
    private BooleanExpression memberIdEq(String memberId) {
        if (StringUtils.hasText(memberId)) {
            try {
                Long memberIdLong = Long.parseLong(memberId);
                return snsPostEntity.memberId.eq(memberIdLong);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private BooleanExpression categoryEq(String category) {
        return StringUtils.hasText(category) ? snsPostEntity.category.eq(category) : null;
    }
}
