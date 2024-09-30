package com.example.twinklesns.domain.entity.like;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostLikeEntity is a Querydsl query type for PostLikeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostLikeEntity extends EntityPathBase<PostLikeEntity> {

    private static final long serialVersionUID = -988259601L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostLikeEntity postLikeEntity = new QPostLikeEntity("postLikeEntity");

    public final QPostLikeId id;

    public QPostLikeEntity(String variable) {
        this(PostLikeEntity.class, forVariable(variable), INITS);
    }

    public QPostLikeEntity(Path<? extends PostLikeEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostLikeEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostLikeEntity(PathMetadata metadata, PathInits inits) {
        this(PostLikeEntity.class, metadata, inits);
    }

    public QPostLikeEntity(Class<? extends PostLikeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QPostLikeId(forProperty("id")) : null;
    }

}

