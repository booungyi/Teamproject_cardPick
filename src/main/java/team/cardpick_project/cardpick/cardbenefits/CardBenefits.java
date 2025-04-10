package team.cardpick_project.cardpick.cardbenefits;

import jakarta.persistence.*;
import lombok.*;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

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
    private CardPick cardPick;

    public CardBenefits(String benefitName, String benefitDetail, CardPick cardPick) {
        this.benefitName = benefitName;
        this.benefitDetail = benefitDetail;
        this.cardPick = cardPick;
        cardPick.getCardBenefitsList().add(this);
    }
}
