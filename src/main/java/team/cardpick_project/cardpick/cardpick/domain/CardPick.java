package team.cardpick_project.cardpick.cardpick.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CardPick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String cardName;

    @Column(nullable = false)
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

    //광고
    @OneToMany(mappedBy = "cardPick")
    private List<Advertise> advertiseList = new ArrayList<>();

    public void addCategory(List<CardCategory> categories){
        this.cardCategories.addAll(categories);
    }
}
