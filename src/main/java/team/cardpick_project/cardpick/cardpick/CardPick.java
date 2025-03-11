package team.cardpick_project.cardpick.cardpick;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class CardPick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String annualFee;

    @Column(nullable = false)
    private String applyLink;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String detailUrl;

    @CreatedBy
    @Column(nullable = true)
    private LocalDateTime createAt;


}
