"use client";

import { useState, useEffect } from "react";
import { Card } from "@/app/page";
import styles from "./PopularCards.module.css";

const PopularCards = ({ cards }: { cards?: Card[] }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [fade, setFade] = useState(true);
  const [isExpanded, setIsExpanded] = useState(false);

  useEffect(() => {
    if (!cards || cards.length === 0) return;

    const interval = setInterval(() => {
      setFade(false); // 🔹 먼저 사라지게 만들고
      setTimeout(() => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % cards.length);
        setFade(true); // 🔹 새로운 카드 등장
      }, 500); // 0.5초 후 카드 변경
    }, 4000); // ⏳ 4초마다 변경

    return () => clearInterval(interval);
  }, [cards]);

  return (
    <div className={styles.popularCardsContainer}>
      <h3 className={styles.title} onClick={() => setIsExpanded(!isExpanded)}>
        {/*신용카드 실시간 인기순위*/}{" "}
        {isExpanded ? (
          <ul className={styles.listContainer}>
            <p className={styles.popularCardsTitle}> 실시간 신용카드 TOP 10</p>
            {cards && cards.length > 0 ? (
              cards.map((card, index) => (
                <li key={index} className={styles.listItem}>
                  <span className="font-bold">{index + 1}.</span>{" "}
                  {card.cardName}
                </li>
              ))
            ) : (
              <li className={styles.emptyMessage}>카드 정보 없음</li>
            )}
          </ul>
        ) : (
          <div
            className={`${styles.currentCard} ${fade ? styles.fadeIn : styles.fadeOut}`}
          >
            {cards && cards.length > 0 ? (
              <span>
                {currentIndex + 1}. {cards[currentIndex]?.cardName}
              </span>
            ) : (
              <span className={styles.emptyMessage}>카드 정보 없음</span>
            )}
          </div>
        )}
      </h3>
    </div>
  );
};

export default PopularCards;
