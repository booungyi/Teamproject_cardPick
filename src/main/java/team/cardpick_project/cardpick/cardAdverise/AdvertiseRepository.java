package team.cardpick_project.cardpick.cardAdverise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AdvertiseRepository extends JpaRepository<Advertise,Long> {
    List<Advertise> findByAdStatusIn(List<AdStatus> statuses);

    List<Advertise> findByIsDeletedFalse();

    @Query("SELECT COUNT(a) FROM Advertise a WHERE a.cardPick = :cardPick " +
            "AND ((a.startDate BETWEEN :startDate AND :endDate) OR (a.endDate BETWEEN :startDate AND :endDate))")
    int countByCardPickAndDateOverlap(@Param("cardPick") CardPick cardPick,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}
