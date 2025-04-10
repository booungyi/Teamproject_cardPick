"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import styles from "./quizResults.module.css";
// import Image from "next/image";
// import styles from "@/app/selectedBenefit/styles.module.css";

// 카드 정보 인터페이스
interface Card {
  id: number; // 카드 ID
  cardName: string;
  imageUrl: string;
  detailUrl: string;
  hasEvent?: boolean; // 이벤트 진행 여부
  isAdCard?: boolean; // 광고 카드 여부
}

// 카테고리 정보 인터페이스
interface CategoryInfo {
  title: string;
  benefits: string[];
  cards: Card[];
}

// 추천 정보 인터페이스
interface Recommendations {
  [key: string]: {
    title: string;
    benefits: string[];
  };
}

const recommendations: Recommendations = {
  J: {
    title: "철저한 예산 관리자를 위한 추천",
    benefits: [
      "매일 쓰기 좋은 생활비 할인 카드 찾아요",
      "공과금, 통신비 등 '고정비'를 묶고 싶어요",
    ],
  },
  P: {
    title: "자유로운 소비자를 위한 추천",
    benefits: [
      "다양한 혜택을 제공하는 카드 찾아요",
      "쇼핑할 때 혜택이 많은 카드 원해요",
    ],
  },
  T: {
    title: "분석적인 소비자를 위한 추천",
    benefits: [
      "혜택을 꼼꼼히 따지고 분석해요",
      "최대 할인을 받을 수 있는 카드 찾아요",
    ],
  },
  F: {
    title: "감성적인 소비자를 위한 추천",
    benefits: [
      "디자인이나 감성적인 요소가 중요해요",
      "감성적인 혜택이 많은 카드 찾아요",
    ],
  },
  S: {
    title: "집중적인 소비자를 위한 추천",
    benefits: [
      "한 가지 카드만 집중적으로 사용해요",
      "특정 영역에서 최대 할인을 받아요",
    ],
  },
  N: {
    title: "다양한 혜택을 추구하는 소비자를 위한 추천",
    benefits: [
      "여러 카드를 사용하며 혜택을 최대로 활용해요",
      "다양한 혜택을 제공하는 카드 찾아요",
    ],
  },
};

export default function Results() {
  const router = useRouter();
  const [categoryInfo, setCategoryInfo] = useState<CategoryInfo | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pages, setPages] = useState<Card[][]>([]);
  const [activePage, setActivePage] = useState(0);

  useEffect(() => {
    // MBTI 결과 가져오기
    try {
      const storedAnswers = localStorage.getItem("quizAnswers");
      if (storedAnswers) {
        const answers = JSON.parse(storedAnswers);
        fetchCardRecommendations(answers);
      } else {
        router.push("/quiz");
      }
    } catch (err) {
      console.error("로컬 스토리지 오류:", err);
      setError("사용자 정보를 불러오는데 실패했습니다.");
      setLoading(false);
    }
  }, [router]);

  const fetchCardRecommendations = async (answers: string[]) => {
    try {
      console.log("API 요청 시작");

      // 사용자 유형 결정
      const frequency: { [key: string]: number } = {};
      answers.forEach((answer) => {
        frequency[answer] = (frequency[answer] || 0) + 1;
      });
      const sortedTypes = Object.keys(frequency).sort(
        (a, b) => frequency[b] - frequency[a],
      );
      const userType = sortedTypes[0];

      console.log("사용자 유형:", userType);

      // API 호출
      const response = await fetch(
        `http://localhost:8080/api/card_picks/mbti?mbti=${userType}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        },
      );

      if (!response.ok) {
        throw new Error(`API 응답 오류: ${response.status}`);
      }

      const data = await response.json();
      console.log("API 응답 데이터:", data);

      // 데이터 전처리: 중복 카드 제거 및 이벤트 속성 추가
      const uniqueCards = removeDuplicateCards(data);

      // 특정 카드에 이벤트 속성 추가
      const cardsWithEvents = addEventFlags(uniqueCards);

      // 카드를 5개씩 그룹화
      const groupedCards = groupCardsIntoPages(cardsWithEvents, 5);
      setPages(groupedCards);

      // 최종 데이터 구성
      const recommendation = recommendations[userType] || recommendations.J;
      setCategoryInfo({
        title: recommendation.title,
        benefits: recommendation.benefits,
        cards: cardsWithEvents, // 전체 카드 목록
      });
    } catch (error) {
      console.error("카드 정보 가져오기 실패:", error);
      setError("카드 정보를 불러오는데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // 중복 카드 제거 함수
  const removeDuplicateCards = (data: any): Card[] => {
    const uniqueCards: Card[] = [];
    const cardNames = new Set();

    if (Array.isArray(data)) {
      // API가 배열을 직접 반환하는 경우
      data.forEach((cardPick) => {
        if (!cardNames.has(cardPick.cardName)) {
          cardNames.add(cardPick.cardName);
          uniqueCards.push({
            id: cardPick.id,
            cardName: cardPick.cardName,
            imageUrl: cardPick.imageUrl,
            detailUrl: cardPick.detailUrl,
            isAdCard: cardPick.isAdCard, //광고 추가
          });
        }
      });
    } else if (data.cards && Array.isArray(data.cards)) {
      // API가 cards 배열을 포함한 객체를 반환하는 경우
      data.cards.forEach((cardPick: any) => {
        if (!cardNames.has(cardPick.cardName)) {
          cardNames.add(cardPick.cardName);
          uniqueCards.push({
            id: cardPick.id,
            cardName: cardPick.cardName,
            imageUrl: cardPick.imageUrl,
            detailUrl: cardPick.detailUrl,
          });
        }
      });
    }

    return uniqueCards;
  };

  // 이벤트 플래그 추가 함수
  const addEventFlags = (cards: Card[]): Card[] => {
    return cards.map((cardPick) => ({
      ...cardPick,
      hasEvent: [""].includes(cardPick.cardName),
    }));
  };

  // 카드를 그룹화하는 함수
  const groupCardsIntoPages = (cards: Card[], groupSize: number): Card[][] => {
    const pages: Card[][] = [];
    for (let i = 0; i < cards.length; i += groupSize) {
      pages.push(cards.slice(i, i + groupSize));
    }
    return pages;
  };

  // 카드 클릭 시 상세 페이지로 이동하는 핸들러

  // ✅ 상세 페이지로 이동하는 함수
  const handleCardClick = (id: number) => {
    router.push(`/card-benefit/${id}`);
  };

  // 페이지 변경 핸들러
  const handlePageChange = (pageIndex: number) => {
    setActivePage(pageIndex);
  };

  if (loading) {
    return <div className={styles.loading}>카드 정보를 불러오는 중...</div>;
  }

  if (error) {
    return <div className={styles.error}>{error}</div>;
  }

  return (
    <div className={styles.container}>
      {/* 진행 단계 표시 */}
      <div className={styles.progressSteps}>
        {pages.map((_, index) => (
          <div
            key={index}
            className={`${styles.step} ${activePage === index ? styles.active : ""}`}
            onClick={() => handlePageChange(index)}
          >
            <div className={styles.stepCircle}>추천 {index + 1}</div>
          </div>
        ))}
      </div>

      {categoryInfo ? (
        <div className={styles.recommendationSection}>
          {/* 추천 제목 */}
          <h1 className={styles.mainTitle}>{categoryInfo.title}</h1>

          {/* 혜택 포인트 */}
          {categoryInfo.benefits && categoryInfo.benefits.length > 0 && (
            <div className={styles.benefitPoints}>
              {categoryInfo.benefits.map((benefit, index) => (
                <div key={index} className={styles.benefitItem}>
                  <span className={styles.checkmark}>✓</span>
                  <span>{benefit}</span>
                </div>
              ))}
            </div>
          )}

          {/* 카드 컨테이너 */}
          {pages[activePage] && pages[activePage].length > 0 && (
            <div className={styles.cardContainer}>
              {pages[activePage].map((cardPick, index) => (
                <div
                  key={`${cardPick.cardName}-${index}`}
                  className={styles.cardItem}
                  onClick={() => handleCardClick(cardPick.id)}
                >
                  <div className={styles.cardImageWrapper}>
                    {cardPick.hasEvent && (
                      <div className={styles.eventBadge}>
                        이벤트
                        <br />
                        진행중
                      </div>
                    )}
                    {cardPick.isAdCard && (
                      <div className={styles.adBadge}>광고</div> // 👈 광고 뱃지 추가
                    )}

                    <div className={styles.cardImage}>
                      <img
                        src={cardPick.imageUrl}
                        alt={cardPick.cardName}
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                          objectPosition: "center",
                        }}
                        onError={(e) => {
                          e.currentTarget.onerror = null;
                          e.currentTarget.src =
                            "/images/cardPick-placeholder.jpg";
                        }}
                      />
                    </div>
                  </div>
                  <h3 className={styles.cardName}>{cardPick.cardName}</h3>
                </div>
              ))}
            </div>
          )}
        </div>
      ) : (
        <div>카드 정보를 불러오는데 실패했습니다.</div>
      )}

      <button onClick={() => router.push("/")} className={styles.homeButton}>
        홈으로 돌아가기
      </button>
    </div>
  );
}
