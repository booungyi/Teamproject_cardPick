package team.cardpick_project.cardpick.cardAdverise;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

import java.time.LocalDateTime;
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
    public void CountExistingAds(LocalDateTime start, LocalDateTime end) {
        // 시작 날짜와 종료 날짜를 LocalDateTime으로 변환
        // start와 end 날짜를 00:00:00 시점으로 설정하는 이유는:
        // 1. 날짜만 비교하려는 목적이기 때문에, 시작일과 종료일을 00:00:00 시점으로 맞추어 범위를 정확히 정의하고,
        // 2. 날짜가 변경될 때마다 시간대나 시간 정보를 고려하지 않고,
        // 그날 의 전체 범위(00:00:00부터 23:59:59까지)를 포함하도록 하기 위함.
        LocalDateTime currentDate = start.toLocalDate().atStartOfDay(); // 시작 날짜의 00:00:00
        LocalDateTime endDate = end.toLocalDate().atStartOfDay(); // 종료 날짜의 00:00:00

        //start부터 end까지 하루씩 증가하면서 반복해야 하므로,
        //반복 횟수가 정해져 있지 않은 경우에는 for문보다 while문이 더 적절합
        while (!currentDate.isAfter(endDate)) {
            Long count = jpaQueryFactory
                    .select(advertise.count())
                    .from(advertise)
                    .where(advertise.isDeleted.isFalse(),
                            advertise.startDate.loe(currentDate.plusDays(1).minusNanos(1)),
                            advertise.endDate.goe(currentDate))
                    .fetchOne();
            if (count != null && count >= 5) {
                throw new IllegalArgumentException("광고는 최대 5개까지 등록 가능합니다.");
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    public List<CardPick> findActiveAdCard(LocalDateTime today) {
        List<Advertise> advertiseList = jpaQueryFactory.selectFrom(this.advertise)
                .where(
                        this.advertise.startDate.loe(today),
                        this.advertise.endDate.goe(today),
                        this.advertise.adStatus.eq(AdStatus.ACTIVE)
                )
                .fetch();

        if (advertiseList == null || advertiseList.isEmpty()) {
            return List.of();
        }
        // 리스트에서 모든 CardPick을 추출
        return advertiseList.stream()
                .map(advertise -> advertise.getCardPick())
                .collect(Collectors.toList());
    }
}
