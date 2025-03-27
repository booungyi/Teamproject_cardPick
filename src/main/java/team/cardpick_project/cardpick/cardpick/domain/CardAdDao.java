package team.cardpick_project.cardpick.cardpick.domain;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardAdDao {
    private final JPAQueryFactory jpaQueryFactory;
    private final QCardAd qCardAd = QCardAd.cardAd;

    public List<Long> getCards() {
        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay(); // 오늘 00:00:00
        LocalDateTime todayEnd = LocalDateTime.now().toLocalDate().atTime(23, 59, 59); // 오늘 23:59:59

        List<Long> cards = jpaQueryFactory
                .select(qCardAd.id)
                .from(qCardAd)
                .where(qCardAd.startDate.loe(todayEnd).and(
                        qCardAd.endDate.goe(todayStart)
                )).fetch();
        return cards;
//        return jpaQueryFactory
//                .selectFrom(qCardAd)
//                .where(qCardAd.id.in(fetch)) // ID 리스트로 조회
//                .fetch();
    }
    public Long getCardOne(Long cardId){
       Long result = jpaQueryFactory
                .select(qCardAd.id)
                .from(qCardAd)
                .where(qCardAd.id.eq(cardId))
                .fetchOne();
        if (result == null) {
            throw new EntityNotFoundException("CardAd with id " + cardId + " not found");
        }
        return result;
    }

}
