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

    @Column(nullable = false)
    private boolean isDeleted = false;

    // 광고:카드 = n:1
    @ManyToOne
    @JoinColumn(name = "card_pick_id")
    private CardPick cardPick;

    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)) {
            this.adStatus = AdStatus.PENDING;
        } else if (now.isAfter(endDate)) {
            deleted();
        } else {
            this.adStatus = AdStatus.ACTIVE;
        }
    }

    public Advertise() {
    }

    public void setAdStatus(AdStatus adStatus) {
        this.adStatus = adStatus;
    }

    //    // ID만 받아서 생성하는 생성자
    public Advertise(CardPick cardPick, LocalDateTime start, LocalDateTime end) {
        this.cardPick = cardPick;
        this.startDate = start;
        this.endDate = end;
    }

    // test 용 또는 수동으로 delete 를 해야될 때 + 함수 최적화
    // isDeleted = true 로 변경
    // AdStatus 를 END 로 저장
    public void deleted() {
        this.isDeleted = true;
        this.adStatus = AdStatus.END;
    }
}
