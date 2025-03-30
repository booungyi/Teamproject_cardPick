package team.cardpick_project.cardpick.cardbenefits;

import jakarta.persistence.*;
import lombok.*;
import team.cardpick_project.cardpick.card.domain.Card;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CardBenefits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String benefitName;

    @Column
    @NonNull
    private String benefitDetail;

    @ManyToOne
    @ToString.Exclude
    private Card card;

    public CardBenefits(String benefitName, String benefitDetail, Card card) {
        this.benefitName = benefitName;
        this.benefitDetail = benefitDetail;
        this.card = card;
        card.getCardBenefitsList().add(this);
    }
}
