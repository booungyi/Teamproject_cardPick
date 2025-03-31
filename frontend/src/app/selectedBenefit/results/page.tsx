'use client';

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import styles from "@/app/selectedBenefit/results/Results.module.css";

// 카드 정보를 포함하는 객체 (categories 제거)
interface CardInfo {
    cardName: string;
    imageUrl: string;
    detailUrl: string;
    hasEvent?: boolean; // 이벤트 진행 여부
}

export default function Results() {
    const [cards, setCards] = useState<CardInfo[]>([]); // 전체 카드 목록
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [pages, setPages] = useState<CardInfo[][]>([]); // 카드 페이지 배열
    const [activePage, setActivePage] = useState(0); // 현재 페이지

    const router = useRouter();
    const searchParams = useSearchParams();
    const categories = searchParams.getAll("categories");

    // API 호출 및 데이터 처리
    useEffect(() => {
        const fetchFilteredCards = async (categories: string[]) => {
            try {
                setLoading(true);

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

                // 카드 페이지로 그룹화 (5개씩)
                const groupedPages = groupCardsIntoPages(data, 5);
                setPages(groupedPages);
            } catch (error) {
                console.error("카드 데이터 가져오기 실패:", error);
                setError("카드 정보를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchFilteredCards(categories);
    }, []); // categories 값이 변경될 때마다 실행

    // 카드를 5개씩 그룹화하는 함수
    const groupCardsIntoPages = (cards: CardInfo[], groupSize: number): CardInfo[][] => {
        const pages: CardInfo[][] = [];
        for (let i = 0; i < cards.length; i += groupSize) {
            pages.push(cards.slice(i, i + groupSize));
        }
        return pages;
    };

    // 로딩 상태 표시
    if (loading) {
        return <div className={styles.loading}>카드 정보를 불러오는 중...</div>;
    }

    // 에러 상태 표시
    if (error) {
        return <div className={styles.error}>{error}</div>;
    }

    // 페이지 변경 핸들러
    const handlePageChange = (direction: 'prev' | 'next') => {
        setActivePage(prevPage => {
            if (direction === 'prev') {
                return Math.max(prevPage - 1, 0);  // 최소 0 페이지
            } else {
                return Math.min(prevPage + 1, pages.length - 1);  // 최대 페이지 제한
            }
        });
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h2 className={styles.title}>검색된 카드 목록</h2>
            </header>

            <main className={styles.main}>
                <div className={styles.cardGrid}>
                    {pages[activePage] && pages[activePage].map((cardPick) => (
                        <div key={cardPick.detailUrl} className={styles.cardItem}
                             onClick={() => window.open(cardPick.detailUrl, '_blank')}>
                            <img
                                src={cardPick.imageUrl}
                                alt={cardPick.cardName}
                                className={styles.cardImage}
                                onError={(e) => {
                                    e.currentTarget.onerror = null;
                                    e.currentTarget.src = '/images/cardPick-placeholder.jpg';
                                }}
                            />
                            <h3 className={styles.cardName}>{cardPick.cardName}</h3>
                        </div>
                    ))}
                </div>
            </main>

            {/* 페이지 버튼 */}
            <footer className={styles.footer}>
                <div className={styles.pageButtons}>
                    <button
                        onClick={() => handlePageChange('prev')}
                        disabled={activePage === 0} // 첫 페이지에서는 '이전' 버튼 비활성화
                    >
                        이전
                    </button>
                    <span>{activePage + 1} / {pages.length}</span>
                    <button
                        onClick={() => handlePageChange('next')}
                        disabled={activePage === pages.length - 1} // 마지막 페이지에서는 '다음' 버튼 비활성화
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
