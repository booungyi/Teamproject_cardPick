'use client';

    import { useEffect, useState } from "react";
    import { useRouter, useSearchParams } from "next/navigation";
    import styles from "@/styles/Results.module.css";

    // 카드 정보를 포함하는 객체
    interface CardInfo {
        cardName: string;
        imageUrl: string;
        detailUrl: string;
        hasEvent?: boolean; // 이벤트 진행 여부
    }

    export default function Results() {
        const [cards, setCards] = useState<CardInfo[]>([]);
        const [loading, setLoading] = useState(true);
        const [error, setError] = useState<string | null>(null);
        const router = useRouter();
        const searchParams = useSearchParams();

        useEffect(() => {
            const categories = searchParams.get('categories');
            if (categories) {
                fetchFilteredCards(JSON.parse(categories));
            } else {
                setError("카테고리 정보가 없습니다.");
                setLoading(false);
            }
        }, [searchParams]);

        // 선택된 혜택에 맞는 카드 정보 가져오는 함수
        const fetchFilteredCards = async (categories: string[]) => {
            try {
                const queryString = `categories=${categories.join(",")}`;
                const response = await fetch(`http://localhost:8080/api/card_picks/conditions?${queryString}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
                });

                if (!response.ok) {
                    throw new Error(`API 응답 오류: ${response.status}`);
                }

                const data = await response.json();
                setCards(data);
            } catch (error) {
                console.error("카드 데이터 가져오기 실패:", error);
                setError("카드 정보를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
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
                        {cards.map((card, index) => (
                            <div key={index} className={styles.cardItem} onClick={() => window.open(card.detailUrl, '_blank')}>
                                <div className={styles.cardImageWrapper}>
                                    {card.hasEvent && (
                                        <div className={styles.eventBadge}>
                                            이벤트<br />진행중
                                        </div>
                                    )}
                                    <div className={styles.cardImage}>
                                        <img
                                            src={card.imageUrl}
                                            alt={card.cardName}
                                            className={styles.cardImageContent}
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
                </main>
                <footer className={styles.footer}>
                    <button onClick={() => router.push("/")} className={styles.homeButton}>
                        홈으로 돌아가기
                    </button>
                </footer>
            </div>
        );
    }