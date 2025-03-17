'use client';

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import styles from './styles.module.css';
import {FaTags} from "react-icons/fa";
import Link from "next/link";

// 백엔드 enum과 일치하는 카테고리 타입
type Category = '쇼핑' | '교통' | '통신' | '할인_및_적립' | '주유' | '항공' | '음식';

// 카드 정보를 포함하는 객체
interface CardInfo {
    cardName: string;
    imageUrl: string;
    detailUrl: string;
}

// 카테고리 정보를 포함하는 객체
interface CategoryInfo {
    name: Category;
    displayName: string; // UI에 표시할 이름
    icon?: React.ReactNode; // 아이콘 (선택 사항)
}

// 카테고리 데이터 (백엔드 enum과 매핑)
const categories: CategoryInfo[] = [
    {name: '쇼핑', displayName: "쇼핑", icon: "👕"},
    {name: '교통', displayName: "교통", icon: "🚌"},
    {name: '통신', displayName: "통신", icon: "📱"},
    {name: '할인_및_적립', displayName: "할인 및 적립", icon: <FaTags/>},
    {name: '주유', displayName: "주유", icon: "⛽"},
    {name: '항공', displayName: "항공", icon: "✈️"},
    {name: '음식', displayName: "음식", icon: "🍔"}
];

export default function selectedBenefit() {
    const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);
    const [totalCardCount, setTotalCardCount] = useState<number>(0);
    const [filteredCards, setFilteredCards] = useState<CardInfo[]>([]);
    const router = useRouter();

    useEffect(() => {
        // 전체 카드 개수 가져오기
        fetchTotalCardCount();

        // 뒤로가기 버튼을 누르면 메인으로 이동
        const handlePopState = () => {
            router.push("/");
        };
        window.addEventListener("popstate", handlePopState);

        return () => {
            window.removeEventListener("popstate", handlePopState);
        };
    }, [router]);

    useEffect(() => {
        updateURL(selectedCategories);
        fetchFilteredCards(selectedCategories);
    }, [selectedCategories]);

    // 전체 카드 개수 가져오는 함수
    const fetchTotalCardCount = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/card_picks/conditions/count`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            const count = await response.json();
            setTotalCardCount(count);
        } catch (error) {
            console.error("카드 개수 가져오기 실패:", error);
        }
    };

    // 선택된 혜택에 맞는 카드 정보 가져오는 함수
    const fetchFilteredCards = async (categories: Category[]) => {
        if (categories.length === 0) {
            // 선택된 카테고리가 없으면 전체 카드 개수 설정
            setFilteredCards([]);
            return;
        }

        try {
            // categories 배열을 문자열로
            const queryString = `categories=${categories.join(",")}`;

            // GET 요청으로 백엔드에 카드 정보 요청
            const response = await fetch(`http://localhost:8080/api/card_picks/conditions?${queryString}`, {
                method: 'GET',  // GET 요청
                headers: { 'Content-Type': 'application/json' }
            });

            // 응답에서 카드 정보 받아오기
            const cards = await response.json();
            setFilteredCards(cards);  // 필터링된 카드 정보 설정
        } catch (error) {
            console.error("카드 데이터 가져오기 실패:", error);
        }
    };

    // 카테고리 선택/해제 함수
    const toggleCategory = (category: Category) => {
        setSelectedCategories(prev => {
            let updatedCategories;

            if (prev.includes(category)) {
                // 이미 선택된 경우 제거
                updatedCategories = prev.filter(c => c !== category);
            } else {
                // 선택되지 않은 경우 추가
                updatedCategories = [...prev, category];
            }

            return updatedCategories;
        });
    };

    // 검색 초기화 버튼
    const resetSearch = () => {
        setSelectedCategories([]);
        fetchTotalCardCount(); // 전체 카드 개수 다시 가져오기
        setFilteredCards([]); // 필터링된 카드 정보 초기화
    };

    // URL 업데이트 함수
    const updateURL = (categoryNames: Category[]) => {
        // 기본 URL 구조 설정
        const baseUrl = `${window.location.origin}/search/condition`;
        const url = new URL(baseUrl);

        // 항상 cate=CRD 파라미터 추가 - 필요없는 부분???
        url.searchParams.set('cate', 'CRD');

        // 선택된 카테고리가 있으면 search_benefit 파라미터 추가
        if (categoryNames.length > 0) {
            url.searchParams.set('categories', categoryNames.join(','));
        }

        // 페이지 리로드 없이 URL 업데이트
        window.history.pushState(null, '', url.toString());
    };

    return (
        <div className={styles.container}>
            {/* 혜택 선택 부분 */}
            <div className={styles.content}>
                <h2 className={styles.title}>카드 혜택 선택</h2>
                <div className={styles.benefitsGrid}>
                    {categories.map((category) => (
                        <div
                            key={category.name}
                            className={`${styles.benefitCard} ${selectedCategories.includes(category.name) ? styles.selected : ''}`}
                            onClick={() => toggleCategory(category.name)}
                        >
                            {category.icon && (
                                <div className={styles.iconWrapper}>
                                    <span className={styles.icon}>{category.icon}</span>
                                </div>
                            )}
                            <div className={styles.benefitName}>{category.displayName}</div>
                            <button className={styles.selectButton}>
                                {selectedCategories.includes(category.name) ? '선택됨' : '선택하기'}
                            </button>
                        </div>
                    ))}
                </div>
            </div>

            <div className={styles.sidebar}>
                <h3>🔍 검색 결과</h3>
                <span className={styles.cardCount}>맞춤 카드: {selectedCategories.length === 0 ? totalCardCount : filteredCards.length}개</span>
                <div>
                    <Link href="/search/results" className={styles.serchresultButton}>
                        검색된 카드 목록 보기
                    </Link>
                </div>
                {selectedCategories.length > 0 && (
                    <button className={styles.resetButton} onClick={resetSearch}>검색 초기화</button>
                )}
            </div>
        </div>
    );
}