package team.cardpick_project.cardpick.cardbenefits;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardBenefitsDao {
    private final JPAQueryFactory jpaQueryFactory;
    private QCardBenefits qCardBenefits = QCardBenefits.cardBenefits;


    public List<CardBenefitsQDto> getcardBenefitlist(Long cardPickId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                CardBenefitsQDto.class,
                                qCardBenefits.benefitName
                                , qCardBenefits.benefitDetail
                        )
                )
                .from(qCardBenefits)
                .where(qCardBenefits.cardPick.id.eq(cardPickId))
                .fetch();

    }
}
