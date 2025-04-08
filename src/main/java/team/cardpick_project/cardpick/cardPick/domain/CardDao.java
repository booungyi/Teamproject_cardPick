package team.cardpick_project.cardpick.cardPick.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardPick.cardDto.CardResponseQDto;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardDao {
    private final JPAQueryFactory queryFactory;
    private final QCardPick qCardPick = QCardPick.cardPick;
    private final QCardCategory qCardCategory = QCardCategory.cardCategory;

    public List<CardResponseQDto> getCardsByConditions(List<String> categories) {

        List<Category> categoryList = categories.stream()
                .map(Category::valueOf)
                .toList();

        BooleanBuilder bb = new BooleanBuilder();

        bb.and(JPAExpressions
                .selectOne()
                .from(qCardCategory)
                .where(qCardCategory.cardPick.eq(qCardPick)
                        .and(qCardCategory.category.in(categoryList)))
                .groupBy(qCardCategory.cardPick)
                .having(qCardCategory.cardPick.count().goe((long)categoryList.size()))
                .exists());

        List<CardResponseQDto> cardResponseQDtos = queryFactory
                .select(
                        Projections.constructor(
                                CardResponseQDto.class,
                                qCardPick.id,
                                qCardPick.cardName,
                                qCardPick.imageUrl,
                                qCardPick.detailUrl,
                                qCardPick.clickCount
                        )
                )
                .from(qCardPick)
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
                                qCardPick.id,
                                qCardPick.cardName,
                                qCardPick.imageUrl,
                                qCardPick.detailUrl,
                                qCardPick.clickCount
                        )
                )
                .from(qCardPick)
                .leftJoin(qCardCategory).on(qCardCategory.cardPick.eq(qCardPick))
                .where(bb)
                .fetch();

        return cardResponseQDtos;
    }

    public Long getCountByConditions(List<String> categories) {

        List<Category> categoryList = categories.stream()
                .map(Category::valueOf)
                .toList();

        BooleanBuilder bb = new BooleanBuilder();

//        if (issuer != null){
//            bb.and(qCardPick.cardName.contains(issuer));
//        }

        if (!categories.isEmpty()){
            bb.and(JPAExpressions
                    .selectOne()
                    .from(qCardCategory)
                    .where(qCardCategory.cardPick.eq(qCardPick)
                            .and(qCardCategory.category.in(categoryList)))
                    .groupBy(qCardCategory.cardPick)
                    .having(qCardCategory.cardPick.count().goe((long)categoryList.size()))
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

    //클릭증가가 높은순으로 카드 정렬(인기순)
    public List<CardResponseQDto> getPopularCards() {
        return queryFactory
                .select(Projections.constructor(
                        CardResponseQDto.class,
                        qCardPick.id,
                        qCardPick.cardName,
                        qCardPick.imageUrl,
                        qCardPick.detailUrl,
                        qCardPick.clickCount
                ))
                .from(qCardPick)
                .orderBy(qCardPick.clickCount.desc()) // ⭐ 클릭 수 높은 순 정렬
                .limit(10) // 상위 10개만 가져옴
                .fetch();
    }
}
