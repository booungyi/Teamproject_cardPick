'use client';

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import styles from './styles.module.css';
import Link from "next/link";
import {FaShoppingBag, FaBus, FaMobileAlt, FaGasPump, FaPlane, FaUtensils, FaTags} from "react-icons/fa";

// 백엔드 enum과 일치하는 카테고리 타입
type Category = 1 | 2 | 3 | 4 | 5 | 6 | 7;

// // 카테고리 정보를 포함하는 객체
// interface CategoryInfo {
//     name: Category;
//     displayName: string; // UI에 표시할 이름
//     icon?: React.ReactNode; // 아이콘 (선택 사항)
// }

// 숫자와 백엔드 ENUM 값 매핑 (아이콘 추가)
const categoryMap: Record<number, { name: string; icon: React.ReactNode }> = {
    1: {name: "쇼핑", icon: <FaShoppingBag/>},
    2: {name: "교통", icon: <FaBus/>},
    3: {name: "통신", icon: <FaMobileAlt/>},
    4: {name: "할인_및_적립", icon: <FaTags/>},
    5: {name: "주유", icon: <FaGasPump/>},
    6: {name: "항공", icon: <FaPlane/>},
    7: {name: "음식", icon: <FaUtensils/>}
};

//url에는 숫자로 들어감, 백엔드 요청시 문자열로
// 문자열 → 숫자 변환 (반대 매핑)
const reverseCategoryMap: Record<string, number> = Object.fromEntries(
    Object.entries(categoryMap).map(([key, value]) => [value.name, Number(key)])
);

const categories = Object.entries(categoryMap).map(([key, value]) => ({
    id: Number(key) as Category, // 강제 변환
    displayName: value.name,
    icon: value.icon
}));


export default function selectedBenefit() {
    const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);
    const [totalCardCount, setTotalCardCount] = useState<number>(0);
    const [filteredCardCount, setFilteredCardCount] = useState<number>(0);
    const router = useRouter();

    useEffect(() => {
        fetchTotalCardCount();

        const handlePopState = () => {
            router.push("/");
        };
        window.addEventListener("popstate", handlePopState);

        return () => {
            window.removeEventListener("popstate", handlePopState);
        };
    }, [router]);

    // 전체 카드 개수 가져오는 함수
    // const fetchTotalCardCount = async () => {
    //     try {
    //         const response = await fetch(`http://localhost:8080/api/card_picks/conditions/count`, {
    //             method: 'GET',
    //             headers: { 'Content-Type': 'application/json' }
    //         });
    //         const count = await response.json();
    //         setTotalCardCount(count);
    //         setFilteredCardCount(count);
    //     } catch (error) {
    //         console.error("카드 개수 가져오기 실패:", error);
    //     }
    // };

    const fetchTotalCardCount = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/card_picks/conditions/count`, {
                method: 'GET',
                headers: {'Content-Type': 'application/json'}
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();

            // ⚠️ 백엔드에서 객체를 반환할 수도 있으므로, 숫자가 맞는지 확인
            if (typeof data === "number") {
                setTotalCardCount(data);
                setFilteredCardCount(data);
            } else {
                console.error("Unexpected response format:", data);
            }
        } catch (error) {
            console.error("카드 개수 가져오기 실패:", error);
        }
    };

    const fetchFilteredCards = async (categories: Category[]) => {
        try {
            // 카테고리 ID를 문자열 값으로 변환
            const categoryStrings = categories.map(catId => categoryMap[catId].name);

            // 쿼리 파라미터 구성
            const queryParams = new URLSearchParams();
            if (categoryStrings.length > 0) {
                categoryStrings.forEach(cat => queryParams.append('categories', cat));
            }

            const response = await fetch(`http://localhost:8080/api/card_picks/conditions/count?${queryParams.toString()}`, {
                method: 'GET',
                headers: {'Content-Type': 'application/json'}
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();
            if (typeof data === "number") {
                setFilteredCardCount(data);
            } else {
                console.error("예상치 못한 응답 형식:", data);
            }
        } catch (error) {
            console.error("카드 데이터 가져오기 실패:", error);
        }
    };

    // 카테고리 선택/해제 함수
    const toggleCategory = (category: number) => { // number 타입 허용
        setSelectedCategories(prev => {
            let updatedCategories = prev.includes(category as Category) //강제 변환
                ? prev.filter(c => c !== (category as Category))
                : [...prev, category as Category];

            updateURL(updatedCategories);
            fetchFilteredCards(updatedCategories);

            return updatedCategories;
        });
    };

    // 검색 초기화 버튼
    const resetSearch = () => {
        setSelectedCategories([]);
        updateURL([]);
        fetchTotalCardCount();
    };

    // URL 업데이트 함수
    const updateURL = (categoryNames: Category[]) => {
        // 기본 URL 구조 설정
        const baseUrl = `${window.location.origin}api/card_picks/search/condition`;
        const url = new URL(baseUrl);

        // 선택된 카테고리가 있으면 search_benefit 파라미터 추가
        if (categoryNames.length > 0) {
            url.searchParams.set('search_benefit', categoryNames.join(','));
        } else {
            url.searchParams.delete("search_benefit");
        }

        // 페이지 리로드 없이 URL 업데이트 (디코딩 추가)
        window.history.pushState(null, '', decodeURIComponent(url.toString()));
    };


    return (
        <div className={styles.container}>
            {/* 혜택 선택 부분 */}
            <div className={styles.content}>
                <h2 className={styles.title}>카드 혜택 선택</h2>
                <div className={styles.benefitsGrid}>
                    {categories.map((category) => (
                        <div
                            key={category.id}
                            className={`${styles.benefitCard} ${selectedCategories.includes(category.id) ? styles.selected : ''}`}
                            onClick={() => toggleCategory(category.id)}
                        >
                            <div className={styles.iconWrapper}>{category.icon}</div>
                            <div className={styles.benefitName}>{category.displayName}</div>
                            <button className={styles.selectButton}>
                                {selectedCategories.includes(category.id) ? '선택됨' : '선택하기'}
                            </button>
                        </div>
                    ))}
                </div>

            </div>

            <div className={styles.sidebar}>
                <h3>🔍 검색 결과</h3>
                <span className={styles.cardCount}>맞춤 카드: {filteredCardCount}개</span>
                <div>
                    <Link href="/search/results" className={styles.serchresultButton}>
                        검색된 카드 보기
                    </Link>
                </div>
                {selectedCategories.length > 0 && (
                    <button className={styles.resetButton} onClick={resetSearch}>검색 초기화</button>
                )}
            </div>
        </div>
    );

}