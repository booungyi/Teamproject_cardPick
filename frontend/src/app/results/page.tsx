"use client";

    import { useRouter } from "next/navigation";
    import { useEffect, useState } from "react";
    import Image from "next/image";
    import styles from "@/styles/Results.module.css";

    // 카드 정보 인터페이스
    interface Card {
        cardName: string;
        imageUrl: string;
        detailUrl: string;
        hasEvent?: boolean; // 이벤트 진행 여부
    }

    // 카테고리 정보 인터페이스
    interface CategoryInfo {
        title: string;
        benefits: string[];
        cards: Card[];
    }

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
                answers.forEach(answer => {
                    frequency[answer] = (frequency[answer] || 0) + 1;
                });
                const sortedTypes = Object.keys(frequency).sort((a, b) => frequency[b] - frequency[a]);
                const userType = sortedTypes[0];

                console.log("사용자 유형:", userType);

                // API 호출
                const response = await fetch(`http://localhost:8080/api/card_picks/mbti?mbti=${userType}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

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
                setCategoryInfo({
                    title: "이번달도 생활비 걱정인 픽커님께 추천",
                    benefits: [
                        "매일 쓰기 좋은 생활비 할인 카드 찾아요",
                        "공과금, 통신비 등 '고정비'를 묶고 싶어요"
                    ],
                    cards: cardsWithEvents // 전체 카드 목록
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
                data.forEach(card => {
                    if (!cardNames.has(card.cardName)) {
                        cardNames.add(card.cardName);
                        uniqueCards.push({
                            cardName: card.cardName,
                            imageUrl: card.imageUrl,
                            detailUrl: card.detailUrl
                        });
                    }
                });
            } else if (data.cards && Array.isArray(data.cards)) {
                // API가 cards 배열을 포함한 객체를 반환하는 경우
                data.cards.forEach((card: any) => {
                    if (!cardNames.has(card.cardName)) {
                        cardNames.add(card.cardName);
                        uniqueCards.push({
                            cardName: card.cardName,
                            imageUrl: card.imageUrl,
                            detailUrl: card.detailUrl
                        });
                    }
                });
            }

            return uniqueCards;
        };

        // 이벤트 플래그 추가 함수
        const addEventFlags = (cards: Card[]): Card[] => {
            return cards.map(card => ({
                ...card,
                hasEvent: ['신한카드 Mr.Life', '신한카드 Shopping', 'DA카드의정석II', 'KB국민 Easy Pick카드'].includes(card.cardName)
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
        const handleCardClick = (detailUrl: string) => {
            window.open(detailUrl, '_blank');
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
                        <h1 className={styles.mainTitle}>
                            {categoryInfo.title}
                        </h1>

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
                                {pages[activePage].map((card, index) => (
                                    <div
                                        key={`${card.cardName}-${index}`}
                                        className={styles.cardItem}
                                        onClick={() => handleCardClick(card.detailUrl)}
                                    >
                                        <div className={styles.cardImageWrapper}>
                                            {card.hasEvent && (
                                                <div className={styles.eventBadge}>
                                                    이벤트<br/>진행중
                                                </div>
                                            )}
                                            <div className={styles.cardImage}>
                                                <img
                                                    src={card.imageUrl}
                                                    alt={card.cardName}
                                                    style={{width: '100%', height: '100%', objectFit: 'contain'}}
                                                    onError={(e) => {
                                                        e.currentTarget.onerror = null;
                                                        e.currentTarget.src = '/images/card-placeholder.jpg';
                                                    }}
                                                />
                                            </div>
                                        </div>
                                        <h3 className={styles.cardName}>{card.cardName}</h3>
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