package team.cardpick_project.cardpick.cardpick.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardRecommendationRequest;
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

    public List<CardResponseQDto> getCardsByMbti(String mbti) {

        BooleanBuilder bb = new BooleanBuilder();

        bb.and(JPAExpressions
                .selectOne()
                .from(qCardCategory)
                .where(qCardCategory.cardPick.eq(qCardPick)
                        .and(qCardCategory.category.in(Mbti.fromString(mbti).getCategories())))
                .groupBy(qCardCategory.cardPick)
                .having(qCardCategory.cardPick.count().goe((long)Mbti.fromString(mbti).getCategories().size()))
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

    public Long getCountByConditions(@Valid CardRecommendationRequest rq) {



        List<Category> categories = rq.categories().stream()
                .map(Category::valueOf)
                .toList();

        BooleanBuilder bb = new BooleanBuilder();

        if (rq.issuer() != null){
            bb.and(qCardPick.cardName.contains(rq.issuer()));
        }

        if (!rq.categories().isEmpty()){
            bb.and(JPAExpressions
                    .selectOne()
                    .from(qCardCategory)
                    .where(qCardCategory.cardPick.eq(qCardPick)
                            .and(qCardCategory.category.in(categories)))
                    .groupBy(qCardCategory.cardPick)
                    .having(qCardCategory.cardPick.count().goe((long)categories.size()))
                    .exists());
        }

        Long count = queryFactory
                .select(
                        qCardPick.count()
                )
                .from(qCardPick)
                .where(bb)
                .fetchOne();

        return count;
    }
}
