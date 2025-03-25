package team.cardpick_project.cardpick.cardpick.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Advertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime endDate;

    //광고 상태
    @Enumerated(EnumType.STRING)
    private AdStatus adStatus = AdStatus.PENDING;

    @Setter
    @Column(nullable = false)
    private boolean isDeleted = false;

    //광고:카드 = n:1
    @Setter
    @ManyToOne
    private CardPick cardPick;

    protected Advertise(){}

    public Advertise(LocalDateTime startDate, LocalDateTime endDate, CardPick cardPick) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.cardPick= cardPick;
    }

    //광고 상태 업데이트 함수 + end일 경우 소프트 딜리트
    public void updateStatus(){
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)){
            this.adStatus = AdStatus.PENDING;
        }
        else if (now.isAfter(endDate)){
            this.adStatus = AdStatus.END;
            this.isDeleted = true;
        }
        else {
            this.adStatus = AdStatus.ACTIVE;
        }
    }

    public boolean isDeleted(){
        return isDeleted;
    }


}
