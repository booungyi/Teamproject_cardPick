package team.cardpick_project.cardpick.cardpick.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdvertiseRepository extends JpaRepository<Advertise,Long> {
    int countByCardPickAndStartDateLessThanEqualAndEndDateGreaterThanEqual(CardPick cardPick, LocalDateTime end, LocalDateTime start);

    List<Advertise> findByAdStatusIn(List<AdStatus> statuses);

    Advertise findByCardPickAndIsDeleted(CardPick cardPick, boolean isDeleted);
}
