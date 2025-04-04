package team.cardpick_project.cardpick.cardAdverise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertiseRepository extends JpaRepository<Advertise,Long> {
    List<Advertise> findByAdStatusIn(List<AdStatus> statuses);

    List<Advertise> findByIsDeletedFalse();


}
