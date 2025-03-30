package team.cardpick_project.cardpick.card.domain;

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
    private Card card;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Category category;
}
