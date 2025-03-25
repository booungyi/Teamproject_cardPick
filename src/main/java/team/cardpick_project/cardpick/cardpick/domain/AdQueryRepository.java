package team.cardpick_project.cardpick.cardpick.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AdQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAdvertise advertise = QAdvertise.advertise;

    public AdQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public int CountExistingAds(CardPick cardPick, LocalDateTime start, LocalDateTime end) {

        Long count = jpaQueryFactory
                .select(advertise.count())
                .from(advertise)
                .where(
                        advertise.cardPick.eq(cardPick),    //카드 선택
                        advertise.startDate.loe(end),       // 시작이 종료보다 이전
                        advertise.endDate.goe(start),        // 종료일이 시작보다 이후
                        advertise.isDeleted.isFalse()       //소프트 딜리트 제외
                )
                .fetchOne();

        return count != null ? count.intValue() : 0;
    }

    public CardPick findActiveAdCard(LocalDateTime today) {
        Advertise advertise = jpaQueryFactory.selectFrom(this.advertise)
                .where(
                        this.advertise.startDate.loe(today),
                        this.advertise.endDate.goe(today)
                )
                .fetchOne();

        if (advertise == null){
            return null;
        }
        return advertise.getCardPick();
    }
}
