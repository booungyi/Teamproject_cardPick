package team.cardpick_project.cardpick.cardpick.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@EntityListeners(AuditingEntityListener.class)
public class CardPick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String cardName;

    @NonNull
    @OneToMany(mappedBy = "cardPick",cascade = CascadeType.ALL)
    private List<CardCategory> cardCategories = new ArrayList<>();

    @Column(nullable = false)
    @NonNull
    private String description;

    @Column(nullable = false)
    @NonNull
    private String annualFee;

    //신청 페이지는 일단 보류
//    @Column(nullable = false)
//    @NonNull
//    private String applyLink;

    @Column(nullable = false)
    @NonNull
    private String imageUrl;

    @Column(nullable = false)
    @NonNull
    private String detailUrl;

    @CreatedBy
    @Column(nullable = true)
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_ad_id" , nullable = true)
    private CardAd cardAd;

    public void addCategory(List<CardCategory> categories){
        this.cardCategories.addAll(categories);
    }
}
