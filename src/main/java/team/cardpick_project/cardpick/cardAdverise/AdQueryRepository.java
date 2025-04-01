package team.cardpick_project.cardpick.cardAdverise;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
//    public void CountExistingAds(LocalDateTime start, LocalDateTime end) {
//        // 시작 날짜와 종료 날짜를 LocalDateTime으로 변환
//        // start와 end 날짜를 00:00:00 시점으로 설정하는 이유는:
//        // 1. 날짜만 비교하려는 목적이기 때문에, 시작일과 종료일을 00:00:00 시점으로 맞추어 범위를 정확히 정의하고,
//        // 2. 날짜가 변경될 때마다 시간대나 시간 정보를 고려하지 않고,
//        // 그날 의 전체 범위(00:00:00부터 23:59:59까지)를 포함하도록 하기 위함.
//        LocalDateTime currentDate = start.toLocalDate().atStartOfDay(); // 시작 날짜의 00:00:00
//        LocalDateTime endDate = end.toLocalDate().atStartOfDay(); // 종료 날짜의 00:00:00
//
//        //start부터 end까지 하루씩 증가하면서 반복해야 하므로,
//        //반복 횟수가 정해져 있지 않은 경우에는 for문보다 while문이 더 적절합
//        while (!currentDate.isAfter(endDate)) {
//            Long count = jpaQueryFactory
//                    .select(advertise.count())
//                    .from(advertise)
//                    .where(advertise.isDeleted.isFalse(),
//                            advertise.startDate.loe(currentDate.plusDays(1).minusNanos(1)),
//                            advertise.endDate.goe(currentDate))
//                    .fetchOne();
//            if (count != null && count >= 5) {
//                throw new IllegalArgumentException("광고는 최대 5개까지 등록 가능합니다.");
//            }
//            currentDate = currentDate.plusDays(1);
//        }
//    }

//    public boolean isAdPeriodConflict(LocalDateTime start, LocalDateTime end, Long adId) {
//        // `exists`로 이미 충돌이 발생한 광고가 있는지만 체크
//        BooleanBuilder condition = new BooleanBuilder();
//        condition.and(advertise.isDeleted.isFalse())
//                .and(advertise.startDate.loe(end))
//                .and(advertise.endDate.goe(start))
//                .and(advertise.id.ne(adId));  // 현재 수정 중인 광고 제외
//
//        // exists를 사용하여 광고 존재 여부만 체크 (count로 전체 개수를 구하지 않음)
//        return jpaQueryFactory
//                .selectFrom(advertise)
//                .where(condition)
//                .fetchFirst() != null;  // 첫 번째 결과가 있으면 충돌이 존재
//    }

    public void validateAdPeriod(LocalDateTime start, LocalDateTime end, Long adId) {
        boolean isOverLimit = jpaQueryFactory
                .selectOne()
                .from(advertise)
                .where(advertise.isDeleted.isFalse(),
                        advertise.startDate.loe(end),
                        advertise.endDate.goe(start),
                        advertise.id.ne(adId))
                .groupBy(advertise.startDate)
                .having(advertise.id.count().gt(5))
                .orderBy(advertise.startDate.asc()) // 🔥 날짜 오름차순 정렬 → 8일부터 검사
                .limit(1) // 🚀 가장 먼저 발견된 초과 날짜에서 바로 종료
                .fetchFirst() != null;

        if (isOverLimit) {
            throw new IllegalArgumentException("해당 날짜 중 광고가 5개를 초과하여 변경이 불가능합니다.");
        }
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
