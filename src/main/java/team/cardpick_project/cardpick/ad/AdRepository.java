package team.cardpick_project.cardpick.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.cardpick_project.cardpick.cardpick.domain.CardPick;

import java.time.LocalDate;
import java.util.List;

public interface AdRepository extends JpaRepository<Advertising,Long> {
    List<Advertising> findByStatus(AdStatuse status);

    @Query("SELECT COUNT(a) FROM Advertising a WHERE a.cardPick = :cardPick " +
            "AND ((a.startDate BETWEEN :startDate AND :endDate) OR (a.endDate BETWEEN :startDate AND :endDate))")
    int countByCardPickAndDateOverlap(@Param("cardPick") CardPick cardPick,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
