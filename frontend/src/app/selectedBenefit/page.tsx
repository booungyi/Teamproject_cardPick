"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import styles from "@/app/selectedBenefit/results/Results.module.css";

// 카드 정보를 포함하는 객체
interface CardInfo {
  id: number; // ✅ 카드 ID 추가
  cardName: string;
  imageUrl: string;
  detailUrl: string;
  hasEvent?: boolean;
  isAdCard?: boolean;
}

export default function Results() {
  const [cards, setCards] = useState<CardInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pages, setPages] = useState<CardInfo[][]>([]);
  const [activePage, setActivePage] = useState(0);

  const router = useRouter();
  const searchParams = useSearchParams();
  const categories = searchParams.getAll("categories");

  useEffect(() => {
    const fetchFilteredCards = async (categories: string[]) => {
      try {
        setLoading(true);
        const queryString = categories
          .map((c) => `categories=${encodeURIComponent(c)}`)
          .join("&");
        const requestUrl = `http://localhost:8080/api/card_picks/conditions?${queryString}`;

        console.log("🔍 API 요청 URL:", requestUrl);

        const response = await fetch(requestUrl, {
          method: "GET",
          headers: { "Content-Type": "application/json" },
        });

        if (!response.ok) {
          throw new Error(`API 응답 오류: ${response.status}`);
        }

        const data: CardInfo[] = await response.json();
        console.log("📢 API 응답 데이터:", data); // 광고 카드 포함 여부 확인

        setCards(data);
        setPages(groupCardsIntoPages(data, 5));
      } catch (error) {
        console.error("카드 데이터 가져오기 실패:", error);
        setError("카드 정보를 불러오는데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchFilteredCards(categories);
  }, []);

  const groupCardsIntoPages = (
    cards: CardInfo[],
    groupSize: number,
  ): CardInfo[][] => {
    const pages: CardInfo[][] = [];
    for (let i = 0; i < cards.length; i += groupSize) {
      pages.push(cards.slice(i, i + groupSize));
    }
    return pages;
  };

  // ✅ 상세 페이지로 이동하는 함수
  const handleCardClick = (id: number) => {
    router.push(`/card-benefit/${id}`);
  };

  if (loading) {
    return <div className={styles.loading}>카드 정보를 불러오는 중...</div>;
  }

  if (error) {
    return <div className={styles.error}>{error}</div>;
  }

  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <h2 className={styles.title}>검색된 카드 목록</h2>
      </header>

      <main className={styles.main}>
        <div className={styles.cardGrid}>
          {pages[activePage] &&
            pages[activePage].map((cardPick) => (
              <div
                key={cardPick.id}
                className={`${styles.cardItem} ${cardPick.isAdCard ? styles.adCard : ""}`}
                onClick={() => handleCardClick(cardPick.id)}
              >
                <img
                  src={cardPick.imageUrl}
                  alt={cardPick.cardName}
                  className={styles.cardImage}
                  onError={(e) => {
                    e.currentTarget.onerror = null;
                    e.currentTarget.src = "/images/cardPick-placeholder.jpg";
                  }}
                />
                <h3 className={styles.cardName}>{cardPick.cardName}</h3>
                {cardPick.isAdCard && (
                  <span className={styles.adBadge}>광고</span>
                )}
              </div>
            ))}
        </div>
      </main>

      <footer className={styles.footer}>
        <div className={styles.pageButtons}>
          <button
            onClick={() => setActivePage(Math.max(activePage - 1, 0))}
            disabled={activePage === 0}
          >
            이전
          </button>
          <span>
            {activePage + 1} / {pages.length}
          </span>
          <button
            onClick={() =>
              setActivePage(Math.min(activePage + 1, pages.length - 1))
            }
            disabled={activePage === pages.length - 1}
          >
            다음
          </button>
        </div>

        <button onClick={() => router.push("/")} className={styles.homeButton}>
          홈으로 돌아가기
        </button>
      </footer>
    </div>
  );
}
