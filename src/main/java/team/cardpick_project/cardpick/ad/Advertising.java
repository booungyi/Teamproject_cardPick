package team.cardpick_project.cardpick.ad;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import team.cardpick_project.cardpick.cardpick.domain.CardPick;

import java.time.LocalDate;

@Entity
public class Advertising {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double budget;            // 광고 예산
    private Double spentAmount;       // 소진된 예산
    @Enumerated(EnumType.STRING)
    private AdStatuse status;          // 광고 상태 (ACTIVE, INACTIVE 등)

    @ManyToOne
    @JoinColumn(nullable = false)
    private CardPick cardPick;//카드랑 메핑 1:n


    @CreatedDate
    private LocalDate startDate;      // 광고 시작일
    @LastModifiedDate
    private LocalDate endDate;        // 광고 종료일

    public Advertising(Long aLong, Double budget, Double spentAmount, AdStatuse status, LocalDate startDate, LocalDate endDate) {
    }

    public Advertising( Double budget, Double spentAmount, AdStatuse status) {

        this.budget = budget;
        this.spentAmount = spentAmount;
        this.status = status;

    }

    public Advertising(CardPick cardPick,Double budget, Double spentAmount, AdStatuse status, LocalDate startDate, LocalDate endDate) {
        this.cardPick = cardPick;
        this.budget = budget;
        this.spentAmount = spentAmount;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }



    public Long getId() {
        return id;
    }

    public CardPick getCardPick(){ return cardPick; }

    public Double getBudget() {
        return budget;
    }

    public Double getSpentAmount() {
        return spentAmount;
    }

    public AdStatuse getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // 예산 소진 체크 및 상태 업데이트 메서드
    public boolean isBudgetExceeded() {
        return spentAmount >= budget;
    }

    // 광고 상태 업데이트 메서드
    public void updateAdStatus() {
        if (isBudgetExceeded()) {
            this.status = AdStatuse.INACTIVE;  // 예산 소진 시 광고 비활성화
        }
    }

    // 예산 추가
    public void addSpentAmount(Double amount) {
        this.spentAmount += amount;
        updateAdStatus();  // 예산이 소진되면 상태 업데이트
    }



}



