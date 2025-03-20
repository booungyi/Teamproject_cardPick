'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import styles from './styles.module.css';
import { FaTags } from "react-icons/fa";
import Link from "next/link";

type Category = '쇼핑' | '교통' | '통신' | '할인_및_적립' | '주유' | '항공' | '음식';

interface CardInfo {
    cardName: string;
    imageUrl: string;
    detailUrl: string;
}

interface CategoryInfo {
    name: Category;
    displayName: string;
    icon?: React.ReactNode;
}

const categories: CategoryInfo[] = [
    { name: '쇼핑', displayName: "쇼핑", icon: "👕" },
    { name: '교통', displayName: "교통", icon: "🚌" },
    { name: '통신', displayName: "통신", icon: "📱" },
    { name: '할인_및_적립', displayName: "할인 및 적립", icon: <FaTags /> },
    { name: '주유', displayName: "주유", icon: "⛽" },
    { name: '항공', displayName: "항공", icon: "✈️" },
    { name: '음식', displayName: "음식", icon: "🍔" }
];

export default function SelectedBenefit() {
    const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);
    const [totalCardCount, setTotalCardCount] = useState<number>(0);
    const [filteredCards, setFilteredCards] = useState<CardInfo[]>([]);
    const router = useRouter();

    useEffect(() => {
        fetchTotalCardCount();
    }, []); // 초기 로딩 시 카드 개수 가져오기

    useEffect(() => {
        if (selectedCategories.length > 0) {
            updateURL(selectedCategories);
            fetchFilteredCards(selectedCategories);
        }
    }, [selectedCategories]);

    const fetchTotalCardCount = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/card_picks/conditions/count");
            const count = await response.json();
            setTotalCardCount(count);
        } catch (error) {
            console.error("카드 개수 가져오기 실패:", error);
        }
    };

    const fetchFilteredCards = async (categories: Category[]) => {
        if (categories.length === 0) return; // 카테고리 없으면 요청 안 함

        try {
            const queryString = `categories=${categories.join(",")}`;
            const response = await fetch(`http://localhost:8080/api/card_picks/conditions?${queryString}`);
            const cards = await response.json();
            setFilteredCards(cards);
        } catch (error) {
            console.error("카드 데이터 가져오기 실패:", error);
        }
    };

    const toggleCategory = (category: Category) => {
        setSelectedCategories(prev =>
            prev.includes(category) ? prev.filter(c => c !== category) : [...prev, category]
        );
    };

    const resetSearch = () => {
        setSelectedCategories([]);
        fetchTotalCardCount();
        setFilteredCards([]);
    };

    const updateURL = (categoryNames: Category[]) => {
        const baseUrl = `${window.location.origin}/search/condition`;
        const url = new URL(baseUrl);
        categoryNames.forEach(category => url.searchParams.append('categories', category));
        window.history.pushState(null, '', url.toString());
    };

    return (
        <div className={styles.wrapper}>
            <div className={styles.container}>
                <h2 className={styles.title}>카드 혜택 선택</h2>

                <div className={styles.benefitsGrid}>
                    {categories.map((category) => (
                        <div
                            key={category.name}
                            className={`${styles.benefitCard} ${selectedCategories.includes(category.name) ? styles.selected : ''}`}
                            onClick={() => toggleCategory(category.name)}
                        >
                            {category.icon && <div className={styles.iconWrapper}><span className={styles.icon}>{category.icon}</span></div>}
                            <div className={styles.benefitName}>{category.displayName}</div>
                            <button className={styles.selectButton}>
                                {selectedCategories.includes(category.name) ? '선택됨' : '선택하기'}
                            </button>
                        </div>
                    ))}
                </div>

                <div className={styles.searchResult}>
                    <h3>🔍 검색 결과</h3>
                    <span className={styles.cardCount}>
                        맞춤 카드: {selectedCategories.length === 0 ? totalCardCount : filteredCards.length}개
                    </span>
                    <div>
                        <Link
                            href={{
                                pathname: "/selectedBenefit/results",
                                query: selectedCategories.reduce((acc, category) => {
                                    acc['categories'] = [...(acc['categories'] || []), category];
                                    return acc;
                                }, {} as Record<string, string[]>)
                            }}
                            className={styles.searchResultButton}
                        >
                            검색된 카드 목록 보기
                        </Link>

                    </div>
                    {selectedCategories.length > 0 && (
                        <button className={styles.resetButton} onClick={resetSearch}>검색 초기화</button>
                    )}
                </div>
            </div>
        </div>
    );
}
