package team.cardpick_project.cardpick.cardpick.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardRecommendationRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardResponseQDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardPickDao {
    private final JPAQueryFactory queryFactory;
    private final QCardPick qCardPick = QCardPick.cardPick;
    private final QCardCategory qCardCategory = QCardCategory.cardCategory;

    public List<CardResponseQDto> getCardsByConditions(CardRecommendationRequest rq) {

        List<Category> categories = rq.categories().stream()
                .map(Category::valueOf)
                .toList();

        BooleanBuilder bb = new BooleanBuilder();

        if (rq.issuer() != null){
            bb.and(qCardPick.cardName.contains(rq.issuer()));
        }

        bb.and(JPAExpressions
                .selectOne()
                .from(qCardCategory)
                .where(qCardCategory.cardPick.eq(qCardPick)
                        .and(qCardCategory.category.in(categories)))
                .groupBy(qCardCategory.cardPick)
                .having(qCardCategory.cardPick.count().goe((long)categories.size()))
                .exists());

        List<CardResponseQDto> cardResponseQDtos = queryFactory
                .select(
                        Projections.constructor(
                                CardResponseQDto.class,
                                qCardPick.cardName,
                                qCardPick.imageUrl,
                                qCardPick.detailUrl
                        )
                )
                .from(qCardPick)
                .leftJoin(qCardCategory).on(qCardCategory.cardPick.eq(qCardPick))
                .where(bb)
                .fetch();

        return cardResponseQDtos;
    }
}
