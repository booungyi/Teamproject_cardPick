package team.cardpick_project.cardpick.cardpick.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardPick is a Querydsl query type for CardPick
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardPick extends EntityPathBase<CardPick> {

    private static final long serialVersionUID = -1698362679L;

    public static final QCardPick cardPick = new QCardPick("cardPick");

    public final StringPath annualFee = createString("annualFee");

    public final ListPath<CardCategory, QCardCategory> cardCategories = this.<CardCategory, QCardCategory>createList("cardCategories", CardCategory.class, QCardCategory.class, PathInits.DIRECT2);

    public final StringPath cardName = createString("cardName");

    public final DateTimePath<java.time.LocalDateTime> createAt = createDateTime("createAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final StringPath detailUrl = createString("detailUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public QCardPick(String variable) {
        super(CardPick.class, forVariable(variable));
    }

    public QCardPick(Path<? extends CardPick> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCardPick(PathMetadata metadata) {
        super(CardPick.class, metadata);
    }

}

