"use client";

import { useState, useEffect, useRef } from "react";
import { Card } from "@/app/page";
import styles from "./PopularCards.module.css";
import { useRouter } from "next/navigation";

const PopularCards = ({ cards }: { cards?: Card[] }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [fade, setFade] = useState(true);
  const [isExpanded, setIsExpanded] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);
  const router = useRouter();
  // 🔄 자동 순위 순환
  useEffect(() => {
    if (!cards || cards.length === 0 || isExpanded) return; // 리스트 펼쳐졌을 땐 멈춤

    const interval = setInterval(() => {
      setFade(false);
      setTimeout(() => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % cards.length);
        setFade(true);
      }, 500);
    }, 4000);

    return () => clearInterval(interval);
  }, [cards, isExpanded]);

  //카드 상세 정보 사이트로 이동
  const handleCardClick = (id: number) => {
    setIsExpanded(false); //카드 리스트 닫기
    router.push(`/card-benefit/${id}`);
  };

  // 🧠 리스트 펼친 상태에서 바깥 클릭 시 닫히기
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        isExpanded &&
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setIsExpanded(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [isExpanded]);

  return (
    <div className={styles.popularCardsContainer} ref={containerRef}>
      {!isExpanded ? (
        <div
          className={`${styles.currentCard} ${fade ? styles.fadeIn : styles.fadeOut}`}
          onClick={() => setIsExpanded(true)}
        >
          {cards && cards.length > 0 ? (
            <>
              <span className={styles.rankNumber}>{currentIndex + 1}</span>
              {cards[currentIndex]?.cardName}
            </>
          ) : (
            <span className={styles.emptyMessage}>인기 카드 정보 없음</span>
          )}
        </div>
      ) : (
        <ul
          className={styles.listContainer}
          onClick={(e) => e.stopPropagation()}
        >
          <div className={styles.title}>🏆 실시간 인기 카드</div>
          {cards && cards.length > 0 ? (
            cards.map((card, index) => (
              <li
                key={index}
                className={styles.listItem}
                onClick={() => {
                  handleCardClick(card.id);
                }}
              >
                <span className={styles.rankNumber}>{index + 1}</span>
                <img className={styles.cardImage} src={card.imageUrl} />
                <span>{card.cardName}</span>
              </li>
            ))
          ) : (
            <li className={styles.emptyMessage}>카드 정보가 없습니다</li>
          )}
        </ul>
      )}
    </div>
  );
};

export default PopularCards;
