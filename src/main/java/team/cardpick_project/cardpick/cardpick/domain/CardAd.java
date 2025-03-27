package team.cardpick_project.cardpick.cardpick.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
public class CardAd {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @NonNull
    @Setter(AccessLevel.NONE)
    @JoinColumn(name = "card_id")
    private List<CardPick> cardPicks = new ArrayList<>();

    @NonNull
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime endDate;

    public CardAd(List<CardPick> card, LocalDateTime startDate, LocalDateTime endDate) {
        this.cardPicks = card;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
