package team.cardpick_project.cardpick.cardpick.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPickRepository extends JpaRepository<CardPick, Long> {
    Optional<CardPick> findByCardName(String cardName);


}
