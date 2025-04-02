package team.cardpick_project.cardpick.cardPick.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardPick, Long> {
    CardPick findByCardName(String name);
    Optional<CardPick> findById(Long id);
    }

