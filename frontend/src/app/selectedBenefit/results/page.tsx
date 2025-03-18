'use client';

import {useEffect, useState} from "react";
import {useRouter, useSearchParams} from "next/navigation";
import styles from "@/styles/Results.module.css";

// 카드 정보를 포함하는 객체
interface CardInfo {
    cardName: string;
    imageUrl: string;
    detailUrl: string;
    hasEvent?: boolean; // 이벤트 진행 여부
    categories: string[]; // 카테고리 정보
}

// 추천 정보 인터페이스
interface Recommendations {
    [key: string]: {
        title: string;
        benefits: string[];
    };
}

// 카테고리 타입 정의
type Category = '쇼핑' | '교통' | '통신' | '할인_및_적립' | '주유' | '항공' | '음식';

// 추천 정보 정의
const recommendations: Recommendations = {
    쇼핑: {
        title: "쇼핑 혜택 카드 추천",
        benefits: [
            "다양한 쇼핑 혜택 제공",
            "온라인 쇼핑 시 추가 할인"
        ]
    },
    교통: {
        title: "교통 혜택 카드 추천",
        benefits: [
            "대중교통 및 주차 할인 혜택",
            "교통비 절감을 위한 최적의 카드"
        ]
    },
    통신: {
        title: "통신비 할인 카드 추천",
        benefits: [
            "월 통신비 절감 혜택 제공",
            "가정 및 개인 통신비 할인"
        ]
    },
    할인_및_적립: {
        title: "할인 및 적립 카드 추천",
        benefits: [
            "다양한 구매 할인 혜택 제공",
            "구매 시 포인트 적립 혜택"
        ]
    },
    주유: {
        title: "주유 혜택 카드 추천",
        benefits: [
            "주유소 이용 시 할인 혜택 제공",
            "주유비 절감을 위한 최적의 카드"
        ]
    },
    항공: {
        title: "항공 혜택 카드 추천",
        benefits: [
            "항공 마일리지 적립 혜택 제공",
            "해외 여행 시 추가 할인 및 수수료 면제"
        ]
    },
    음식: {
        title: "음식 혜택 카드 추천",
        benefits: [
            "음식점 및 외식 시 할인 혜택 제공",
            "배달 서비스 이용 시 추가 혜택"
        ]
    }
};

export default function Results() {
    const [cards, setCards] = useState<CardInfo[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [pages, setPages] = useState<CardInfo[][]>([]);
    const [activePage, setActivePage] = useState(0);
    const router = useRouter();
    const searchParams = useSearchParams();

    // API 호출 및 데이터 처리
    useEffect(() => {
        const categories = searchParams.get('categories');
        if (categories) {
            const parsedCategories = JSON.parse(categories);
            fetchFilteredCards(parsedCategories);
        } else {
            setError("카테고리 정보가 없습니다.");
            setLoading(false);
        }
    }, [searchParams]);

    const fetchFilteredCards = async (categories: string[]) => {
        try {
            setLoading(true); // 로딩 상태 활성화
            const queryString = `categories=${categories.join(",")}`;
            const response = await fetch(`http://localhost:8080/api/card_picks/conditions?${queryString}`, {
                method: 'GET',
                headers: {'Content-Type': 'application/json'},
            });

            if (!response.ok) {
                throw new Error(`API 응답 오류: ${response.status}`);
            }

            const data: CardInfo[] = await response.json();

            // 각 카드에 카테고리 정보 추가
            const updatedData = data.map((card) => ({...card, categories}));

            // 중복 제거 (cardName + categories 기준)
            const uniqueCards = Array.from(new Map(updatedData.map((card) => [JSON.stringify({
                cardName: card.cardName,
                categories
            }), card])).values());

            // 카드를 5개씩 그룹화
            const groupedCards = groupCardsIntoPages(uniqueCards, 5);
            setPages(groupedCards);

            setCards(uniqueCards);
        } catch (error) {
            console.error("카드 데이터 가져오기 실패:", error);
            setError("카드 정보를 불러오는데 실패했습니다.");
        } finally {
            setLoading(false); // 로딩 상태 비활성화
        }
    };

    // 카드를 그룹화하는 함수 (5개씩 나누기)
    const groupCardsIntoPages = (cards: CardInfo[], groupSize: number): CardInfo[][] => {
        const pages: CardInfo[][] = [];
        for (let i = 0; i < cards.length; i += groupSize) {
            pages.push(cards.slice(i, i + groupSize));
        }
        return pages;
    };

    // 페이지 변경 핸들러
    const handlePageChange = (pageIndex: number) => {
        setActivePage(pageIndex);
    };

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
                <p>페이지 {activePage + 1} / {pages.length}</p>
            </header>

            <main className={styles.main}>
                <div className={styles.cardGrid}>
                    {pages[activePage]?.map((card, index) => (
                        <div key={index} className={styles.cardItem}
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
                            <p className={styles.cardCategories}>카테고리: {card.categories.join(', ')}</p>
                        </div>
                    ))}
                </div>

                {/* 페이지 전환 버튼 */}
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
            </main>

            <footer className={styles.footer}>
                <button onClick={() => router.push("/")} className={styles.homeButton}>
                    홈으로 돌아가기
                </button>
            </footer>
        </div>
    );
}