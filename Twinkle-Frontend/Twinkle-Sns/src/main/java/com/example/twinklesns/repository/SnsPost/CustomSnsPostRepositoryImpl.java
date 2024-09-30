package com.example.twinklesns.repository.SnsPost;

import com.example.twinklesns.domain.entity.QFileEntity;
import com.example.twinklesns.domain.entity.QSnsPostEntity;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomSnsPostRepositoryImpl implements CustomSnsPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

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

    private BooleanBuilder condition(Map<String, String> searchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(categoryEq(searchCondition.getOrDefault("category", null)));
    }

    private BooleanExpression categoryEq(String category) {
        return StringUtils.hasText(category) ? snsPostEntity.category.eq(category) : null;
    }
}
