package com.example.twinklesns.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSnsPostEntity is a Querydsl query type for SnsPostEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSnsPostEntity extends EntityPathBase<SnsPostEntity> {

    private static final long serialVersionUID = 181814041L;

    public static final QSnsPostEntity snsPostEntity = new QSnsPostEntity("snsPostEntity");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final ListPath<FileEntity, QFileEntity> files = this.<FileEntity, QFileEntity>createList("files", FileEntity.class, QFileEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QSnsPostEntity(String variable) {
        super(SnsPostEntity.class, forVariable(variable));
    }

    public QSnsPostEntity(Path<? extends SnsPostEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSnsPostEntity(PathMetadata metadata) {
        super(SnsPostEntity.class, metadata);
    }

}

