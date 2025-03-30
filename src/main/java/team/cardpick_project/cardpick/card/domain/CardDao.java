package team.cardpick_project.cardpick.card.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.card.cardDto.CardResponseQDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardDao {
    private final JPAQueryFactory queryFactory;
    private final QCard qCard = QCard.card;
    private final QCardCategory qCardCategory = QCardCategory.cardCategory;

    public List<CardResponseQDto> getCardsByConditions(String issuer, List<String> categories) {

        List<Category> categoryList = categories.stream()
                .map(Category::valueOf)
                .toList();

        BooleanBuilder bb = new BooleanBuilder();

        if (issuer != null){
            bb.and(qCard.cardName.contains(issuer));
        }

        bb.and(JPAExpressions
                .selectOne()
                .from(qCardCategory)
                .where(qCardCategory.card.eq(qCard)
                        .and(qCardCategory.category.in(categoryList)))
                .groupBy(qCardCategory.card)
                .having(qCardCategory.card.count().goe((long)categoryList.size()))
                .exists());

        List<CardResponseQDto> cardResponseQDtos = queryFactory
                .select(
                        Projections.constructor(
                                CardResponseQDto.class,
                                qCard.cardName,
                                qCard.imageUrl,
                                qCard.detailUrl
                        )
                )
                .from(qCard)
                .where(bb)
                .fetch();

        return cardResponseQDtos;
    }

    public List<CardResponseQDto> getCardsByMbti(String mbti) {

        BooleanBuilder bb = new BooleanBuilder();

        bb.and(JPAExpressions
                .selectOne()
                .from(qCardCategory)
                .where(qCardCategory.card.eq(qCard)
                        .and(qCardCategory.category.in(Mbti.fromString(mbti).getCategories())))
                .groupBy(qCardCategory.card)
                .having(qCardCategory.card.count().goe((long)Mbti.fromString(mbti).getCategories().size()))
                .exists());

        List<CardResponseQDto> cardResponseQDtos = queryFactory
                .select(
                        Projections.constructor(
                                CardResponseQDto.class,
                                qCard.cardName,
                                qCard.imageUrl,
                                qCard.detailUrl
                        )
                )
                .from(qCard)
                .leftJoin(qCardCategory).on(qCardCategory.card.eq(qCard))
                .where(bb)
                .fetch();

        return cardResponseQDtos;
    }

    public Long getCountByConditions(String issuer, List<String> categories) {



        List<Category> categoryList = categories.stream()
                .map(Category::valueOf)
                .toList();

        BooleanBuilder bb = new BooleanBuilder();

        if (issuer != null){
            bb.and(qCard.cardName.contains(issuer));
        }

        if (!categories.isEmpty()){
            bb.and(JPAExpressions
                    .selectOne()
                    .from(qCardCategory)
                    .where(qCardCategory.card.eq(qCard)
                            .and(qCardCategory.category.in(categoryList)))
                    .groupBy(qCardCategory.card)
                    .having(qCardCategory.card.count().goe((long)categoryList.size()))
                    .exists());
        }

        Long count = queryFactory
                .select(
                        qCard.count()
                )
                .from(qCard)
                .where(bb)
                .fetchOne();

        return count;
    }
}
