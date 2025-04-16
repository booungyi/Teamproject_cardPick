package team.cardpick_project.cardpick.cardAdverise;

import jakarta.persistence.*;
import lombok.*;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Advertise {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime endDate;

    //광고 상태
    @Enumerated(EnumType.STRING)
    private AdStatus adStatus = AdStatus.PENDING;

    //광고배너
//    private AdType adType;
    private String bannerImageUrl;

    @Column(nullable = false)
    private boolean isDeleted = false;

    // 광고:카드 = n:1
    @ManyToOne
    @JoinColumn(name = "card_pick_id")
    private CardPick cardPick;

    private int budget;            // 광고 예산
    private int spentAmount=0;       // 소진된 예산

    public Advertise() {
    }
  
    public Advertise(CardPick cardPick, LocalDateTime start, LocalDateTime end) {
        this.cardPick = cardPick;
        this.startDate = start;
        this.endDate = end;
    }

    public Advertise(LocalDateTime startDate, LocalDateTime endDate, AdStatus adStatus, CardPick cardPick, int budget, int spentAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.adStatus = adStatus;
        this.cardPick = cardPick;
        this.budget = budget;
        this.spentAmount = spentAmount;
    }

//    public void updateStatus() {
//        LocalDateTime now = LocalDateTime.now();
//
//        if (now.isBefore(startDate)) {
//            this.adStatus = AdStatus.PENDING;
//        } else if (now.isAfter(endDate)) {
//            deleted();
//        } else {
//            this.adStatus = AdStatus.ACTIVE;
//        }
//    }

    public void setAdStatus(AdStatus adStatus) {
        this.adStatus = adStatus;
    }

    // test 용 또는 수동으로 delete 를 해야될 때 + 함수 최적화
    // isDeleted = true 로 변경
    // AdStatus 를 END 로 저장
    public void deleted() {
        this.isDeleted = true;
        this.adStatus = AdStatus.END;
    }


    // 예산 소진 체크 및 상태 업데이트 메서드
    public boolean isBudgetExceeded() {
        return spentAmount >= budget;
    }

    // 광고 상태 업데이트 메서드
    public void updateAdStatus() {
        if (isBudgetExceeded()) {
            this.adStatus = AdStatus.INACTIVE;  // 예산 소진 시 광고 비활성화
        }
    }

    // 예산 추가
    public void addSpentAmount(int amount) {
        this.spentAmount += amount;
        updateAdStatus();  // 예산이 소진되면 상태 업데이트
    }

}

