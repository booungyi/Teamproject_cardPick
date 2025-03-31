package team.cardpick_project.cardpick.cardPick.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardPick, Long> {
    CardPick findByCardName(String name);
}
