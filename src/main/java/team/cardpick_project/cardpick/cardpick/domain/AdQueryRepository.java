package team.cardpick_project.cardpick.cardpick.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAdvertise advertise = QAdvertise.advertise;

    public AdQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //수정하려는 날짜에 광고가 다 차있으면 수정하지 못하도록
    //수정할때는 광고카드만 비교
    public void compareAds(CardPick cardPick, LocalDateTime start, LocalDateTime end) {

    }

    public List<CardPick> findActiveAdCard(LocalDateTime today) {
        List<Advertise> advertiseList = jpaQueryFactory.selectFrom(this.advertise)
                .where(
                        this.advertise.startDate.loe(today),
                        this.advertise.endDate.goe(today)
                )
                .fetch();

        if (advertiseList == null || advertiseList.isEmpty()) {
            return null; // 빈 리스트 반환
        }
        // 리스트에서 모든 CardPick을 추출
        return advertiseList.stream()
                .map(advertise -> advertise.getCardPick())
                .collect(Collectors.toList());
    }
}
