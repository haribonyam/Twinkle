package com.example.twinkle.repository.tradeboard;


import com.example.twinkle.domain.entity.QFileEntity;
import com.example.twinkle.domain.entity.QMemberEntity;
import com.example.twinkle.domain.entity.QTradeBoardEntity;
import com.example.twinkle.domain.entity.TradeBoardEntity;
import com.example.twinkle.dto.response.TradeBoardResponseDto;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class CustomTradeBoardRepositoryImpl implements CustomTradeBoardRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QTradeBoardEntity TradeBoard = QTradeBoardEntity.tradeBoardEntity;
    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QFileEntity files = QFileEntity.fileEntity;
    private final QFileEntity files2 = QFileEntity.fileEntity;
    @Override
    public Page<TradeBoardResponseDto> findAllDynamic(Map<String, String> searchCondition, Pageable pageable){
        log.info("page select start");

            List<TradeBoardEntity> result = jpaQueryFactory.selectFrom(TradeBoard)
                    .leftJoin(TradeBoard.member, member)
                    .leftJoin(TradeBoard.files, files)
                    .fetchJoin()
                    .where(condition(searchCondition))
                    .orderBy(TradeBoard.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            List<TradeBoardResponseDto> content = result.stream().map(TradeBoardResponseDto::toDto).collect(Collectors.toList());

        log.info("page select fin");
        JPAQuery<TradeBoardEntity> totalCount = jpaQueryFactory.select(TradeBoard)
                .from(TradeBoard)
                .leftJoin(TradeBoard.member, member).fetchJoin()
                .leftJoin(TradeBoard.files, files).fetchJoin()
                .where(condition(searchCondition));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchCount);

    }

    private BooleanBuilder condition(Map<String, String> cond) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(categoryEq(cond.getOrDefault("category", null)));
    }

    private BooleanExpression categoryEq(String category) {
        return StringUtils.hasText(category) ? TradeBoard.category.eq(category) : null;
    }
}