package team.cardpick_project.cardpick.cardpick.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardCategory is a Querydsl query type for CardCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardCategory extends EntityPathBase<CardCategory> {

    private static final long serialVersionUID = 2004726694L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCardCategory cardCategory = new QCardCategory("cardCategory");

    public final QCardPick cardPick;

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCardCategory(String variable) {
        this(CardCategory.class, forVariable(variable), INITS);
    }

    public QCardCategory(Path<? extends CardCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCardCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCardCategory(PathMetadata metadata, PathInits inits) {
        this(CardCategory.class, metadata, inits);
    }

    public QCardCategory(Class<? extends CardCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cardPick = inits.isInitialized("cardPick") ? new QCardPick(forProperty("cardPick")) : null;
    }

}

