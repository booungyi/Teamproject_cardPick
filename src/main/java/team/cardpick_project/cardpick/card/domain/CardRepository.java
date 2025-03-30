package team.cardpick_project.cardpick.card.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardName(String name);
}
