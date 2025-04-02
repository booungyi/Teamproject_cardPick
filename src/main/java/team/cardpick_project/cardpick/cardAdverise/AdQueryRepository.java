package team.cardpick_project.cardpick.cardAdverise;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.AdDateProjection;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

import java.time.LocalDate;
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


//    public void CountExistingAds(LocalDateTime start, LocalDateTime end) {
//
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

    //수정하려는 날짜에 광고가 다 차있으면 수정하지 못하도록
    //start,end 날짜 사이에 겹치는 광고들 찾아서 반환
    // 겹치는 광고가 있는 날짜 한번에 다 가져옴, 그리고 service에서 비교
    public List<Advertise> findOverlappingAdDates(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .selectFrom(advertise)
                .where(
                        advertise.isDeleted.isFalse(),
                        advertise.startDate.loe(end), // 종료일 이전에 시작된 광고 포함
                        advertise.endDate.goe(start)  // 시작일 이후에 종료된 광고 포함
                )
                .fetch(); // 겹치는 광고들의 시작일과 종료일을 리스트로 반환
//                .stream()
//                .distinct() // 중복된 날짜 제거
//                .collect(Collectors.toList());
    }


    public List<CardPick> findActiveAdCard(LocalDateTime today) {
        List<Advertise> advertiseList = jpaQueryFactory.selectFrom(this.advertise)
                .where(
                        this.advertise.startDate.loe(today),
                        this.advertise.endDate.goe(today)
                )
                .fetch();

        if (advertiseList == null || advertiseList.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }
        // 리스트에서 모든 CardPick을 추출
        return advertiseList.stream()
                .map(advertise -> advertise.getCardPick())
                .collect(Collectors.toList());
    }
}
