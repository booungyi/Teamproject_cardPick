package team.cardpick_project.cardpick.cardPick.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import team.cardpick_project.cardpick.cardAdverise.Advertise;
import team.cardpick_project.cardpick.cardbenefits.CardBenefits;

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
    @ToString.Exclude
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

    @OneToMany(mappedBy = "cardPick")
    @ToString.Exclude
    private List<CardBenefits> cardBenefitsList = new ArrayList<>();

    @CreatedBy
    @Column(nullable = true)
    private LocalDateTime createAt;

    //광고
    @OneToMany(mappedBy = "cardPick")
    private List<Advertise> advertiseList = new ArrayList<>();

    //카드 상세 혜택
    @OneToMany(mappedBy = "cardPick")
    private List<CardBenefits> cardBenefits = new ArrayList<>();

    public void addCategory(List<CardCategory> categories){
        this.cardCategories.addAll(categories);
    }

    // ID만 받는 생성자 추가 (프록시 객체 생성용)
    public CardPick(Long id) {
        this.id = id;
    }

    // 클릭 카운트 필드 추가
    private Integer clickCount=0 ;

    public void incrementClickCount() {
        // 1️⃣ 클릭 수 증가

       clickCount = clickCount+1;


    }

}
