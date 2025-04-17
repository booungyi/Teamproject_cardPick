package team.cardpick_project.cardpick.cardAdverise;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AdQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAdvertise advertise = QAdvertise.advertise;
    private final EntityManager em;

    public AdQueryRepository(JPAQueryFactory jpaQueryFactory, EntityManager em) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.em = em;
    }

    // 아래와 같이 generate_series를 활용한 native query 메서드를 추가합니다.
//    public List<CalendarAdCountDTO> findAdCountsWithGenerateSeries(LocalDateTime start, LocalDateTime end) {
//        // start와 end는 LocalDateTime이지만 generate_series는 날짜 단위이므로 toLocalDate()로 변환
//        String sql = "WITH days AS ( " +
//                "    SELECT generate_series(:startDate, :endDate, interval '1 day') AS ad_day " +
//                ") " +
//                "SELECT d.ad_day AS date, COUNT(a.id) AS count " +
//                "FROM days d " +
//                "LEFT JOIN advertise a " +
//                "  ON a.is_deleted = false " +
//                " AND a.start_date <= (d.ad_day + interval '1 day' - interval '1 second') " +
//                " AND a.end_date >= d.ad_day " +
//                "GROUP BY d.ad_day " +
//                "ORDER BY d.ad_day";
//
//        long beforeTime1 = System.currentTimeMillis();
//
//        List<Object[]> results = em.createNativeQuery(sql)
//                .setParameter("startDate", start.toLocalDate())
//                .setParameter("endDate", end.toLocalDate())
//                .getResultList();
//
//        System.out.println("네이티브 쿼리 걸린 시간 : " + (System.currentTimeMillis() - beforeTime1) / 1000.0 + "초");
//
//        List<CalendarAdCountDTO> list = new ArrayList<>();
//        for (Object[] row : results) {
//            // row[0]는 날짜, row[1]는 광고 수
//            LocalDate date = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
//
//            long count = ((Number) row[1]).longValue();
//            list.add(new CalendarAdCountDTO(date, count));
//        }
//        return list;
//    }

    // 광고 시작일 ~ 종료일 중, 특정 날짜에 유효한 광고 수를 세기 위해 날짜만 추출
//        DateExpression<LocalDate> adDate = Expressions.dateTemplate(
//                LocalDate.class, "date_trunc('day', {0})", advertise.startDate);
    public List<CalendarAdCountDTO> countExistingAds(LocalDateTime start, LocalDateTime end) {

        long beforeTime1 = System.currentTimeMillis();
        // 1. 해당 범위에 걸쳐 있는 광고들을 날짜별로 그룹핑
        List<CalendarAdCountDTO> result = jpaQueryFactory
                .select(Projections.constructor(CalendarAdCountDTO.class,
                        advertise.startDate,
                        advertise.endDate))
                .from(advertise)
                .where(advertise.isDeleted.isFalse()
                        .and(advertise.startDate.loe(end))
                        .and(advertise.endDate.goe(start)))
                .fetch();

        System.out.println("DSL 걸린 시간 : " + (System.currentTimeMillis() - beforeTime1) / 1000.0 + "초");

        return result;
    }


    //이름이 같은 카드에 대해선 광고 제외
    public List<CardPick> findActiveAdCard(LocalDateTime today,
                                           Set<String> excludeNames) {
        List<Advertise> advertiseList = jpaQueryFactory
                .selectFrom(this.advertise)
                .where(
                        this.advertise.startDate.loe(today),
                        this.advertise.endDate.goe(today),
                        this.advertise.adStatus.eq(AdStatus.ACTIVE),
                        excludeNames.isEmpty()  //중복이름 없으면 나머지 3조건식만, 있으면 중복 포함되지 않은 것만
                                ? null
                                : advertise.cardPick.cardName.notIn(excludeNames)
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
