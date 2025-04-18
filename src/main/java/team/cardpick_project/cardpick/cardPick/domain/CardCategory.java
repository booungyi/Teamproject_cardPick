package team.cardpick_project.cardpick.cardPick.domain;

import jakarta.persistence.*;
import lombok.*;
 
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @NonNull
    @ToString.Exclude
    private CardPick cardPick;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Category category;
}
