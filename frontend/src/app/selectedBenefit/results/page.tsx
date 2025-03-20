'use client';

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import styles from "@/styles/Results.module.css";

// 카드 정보를 포함하는 객체 (categories 제거)
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
    const categories = searchParams.getAll("categories");

    // API 호출 및 데이터 처리
    useEffect(() => {
        const fetchFilteredCards = async (categories: string[]) => {
            try {
                setLoading(true);

                // 🔥 URL 확인을 위해 콘솔 출력
                const queryString = categories.map(c => `categories=${encodeURIComponent(c)}`).join("&");
                const requestUrl = `http://localhost:8080/api/card_picks/conditions?${queryString}`;
                console.log("🔍 API 요청 URL:", requestUrl);

                const response = await fetch(requestUrl, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });

                if (!response.ok) {
                    throw new Error(`API 응답 오류: ${response.status}`);
                }

                const data: CardInfo[] = await response.json();
                setCards(data);
            } catch (error) {
                console.error("카드 데이터 가져오기 실패:", error);
                setError("카드 정보를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchFilteredCards(categories);
    }, [categories]); // categories 값이 변경될 때마다 실행

    // 로딩 상태 표시
    if (loading) {
        return <div className={styles.loading}>카드 정보를 불러오는 중...</div>;
    }

    // 에러 상태 표시
    if (error) {
        return <div className={styles.error}>{error}</div>;
    }

    // 카드 목록 렌더링
    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h2 className={styles.title}>검색된 카드 목록</h2>
            </header>

            <main className={styles.main}>
                <div className={styles.cardGrid}>
                    {cards.map((card) => (
                        <div key={card.detailUrl} className={styles.cardItem}
                             onClick={() => window.open(card.detailUrl, '_blank')}>
                            <img
                                src={card.imageUrl}
                                alt={card.cardName}
                                className={styles.cardImage}
                                onError={(e) => {
                                    e.currentTarget.onerror = null;
                                    e.currentTarget.src = '/images/card-placeholder.jpg';
                                }}
                            />
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
