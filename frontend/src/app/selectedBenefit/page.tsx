// 'use client';
//
// import {useEffect, useState} from "react";
// import {useRouter} from "next/navigation";
// import styles from './styles.module.css';
// import {FaShoppingBag, FaBus, FaMobileAlt, FaGasPump, FaPlane, FaUtensils, FaTags} from "react-icons/fa";
//
// // 백엔드 enum과 일치하는 카테고리 타입
// type Category = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7;
//
// // 카테고리 정보를 포함하는 객체
// interface CategoryInfo {
//     name: Category;
//     displayName: string; // UI에 표시할 이름
//     icon?: React.ReactNode; // 아이콘 (선택 사항)
// }
//
// // 카테고리 데이터 (백엔드 enum과 매핑)
// const categories: CategoryInfo[] = [
//     {name: 0, displayName: "쇼핑", icon: "👕"},
//     {name: 1, displayName: "교통", icon: "🚌"},
//     {name: 2, displayName: "통신", icon: "📱"},
//     {name: 3, displayName: "할인_및_적립", icon: <FaTags/>},
//     {name: 4, displayName: "주유", icon: "⛽"},
//     {name: 5, displayName: "항공", icon: "✈️"},
//     {name: 6, displayName: "음식", icon: "🍔"}
// ];
//
// export default function selectedBenefit() {
//     const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);
//     const [allCards, setAllCards] = useState<any[]>([]); // 전체 카드 데이터 저장
//     const [filteredCards, setFilteredCards] = useState<any[]>([]); // 필터링된 카드 데이터
//     const router = useRouter();
//
//     // 페이지 로드될 때 전체 카드 데이터 가져옴
//     useEffect(() => {
//         fetchAllCards();
//
//         // 뒤로 가기 버튼을 누르면 메인으로 이동
//         const handlePopState = () => {
//             router.push("/");
//         };
//         window.addEventListener("popstate", handlePopState);
//
//         return () => {
//             window.removeEventListener("popstate", handlePopState);
//         };
//     }, [router]);
//
//     // 전체 카드 데이터를 가져오는 함수
//     const fetchAllCards = async () => {
//         try {
//             const response = await fetch(`/api/cards`);
//             const data = await response.json();
//             setAllCards(data.cards); // 🔥 전체 카드 저장
//             setFilteredCards(data.cards); // 🔥 처음엔 모든 카드 표시
//         } catch (error) {
//             console.error("카드 데이터 가져오기 실패:", error);
//         }
//     };
//
//     //선택된 혜택에 맞게 카드 데이터 필터링
//     const filterCards = (categories: Category[]) => {
//         if (categories.length === 0) {
//             setFilteredCards(allCards); // 선택된 혜택이 없으면 전체 카드 표시
//         } else {
//             const filtered = allCards.filter(card =>
//                 categories.some(category => card.benefits.includes(category))
//             );
//             setFilteredCards(filtered); // 필터링된 카드 저장
//         }
//     };
//
//
//     // 카테고리 선택/해제 함수
//     const toggleCategory = (category: Category) => {
//         setSelectedCategories(prev => {
//             let updatedCategories;
//
//             if (prev.includes(category)) {
//                 // 이미 선택된 경우 제거
//                 updatedCategories = prev.filter(c => c !== category);
//             } else {
//                 // 선택되지 않은 경우 추가
//                 updatedCategories = [...prev, category];
//             }
//
//             // URL 업데이트
//             updateURL(updatedCategories);
//             filterCards(updatedCategories); //필터링 실행
//
//             return updatedCategories;
//         });
//     };
//
//     //검색 초기화 버튼
//     const resetSearch = () => {
//         setSelectedCategories([]);
//         updateURL([]);
//         setFilteredCards(allCards); //모든 카드 다시 표시
//     };
//
//     // URL 업데이트 함수(혜택 선택했을때 url에 반영, 검색 조건 유지)
//     const updateURL = (categoryNames: Category[]) => {
//         const url = new URL(window.location.href);
//         url.searchParams.set('cate', 'CRD');
//
//         if (categoryNames.length > 0) {
//             url.searchParams.set('search_benefit', categoryNames.join(','));
//         } else {
//             url.searchParams.delete('search_benefit');
//         }
//
//         // 페이지 리로드 없이 URL 업데이트
//         window.history.pushState(null, '', url.toString());
//     };
//
//
//     return (
//         <div className={styles.container}>
//             {/*혜택 선택 부분*/}
//             <tbody>
//             <div className={styles.content}>
//                 <h2 className={styles.title}>카드 혜택 선택</h2>
//                 <div className={styles.benefitsGrid}>
//                     {categories.map((category) => (
//                         <div
//                             key={category.name}
//                             className={`${styles.benefitCard} ${selectedCategories.includes(category.name) ? styles.selected : ''}`}
//                             onClick={() => toggleCategory(category.name)}
//                         >
//                             {category.icon && (
//                                 <div className={styles.iconWrapper}>
//                                     <span className={styles.icon}>{category.icon}</span>
//                                 </div>
//                             )}
//                             <div className={styles.benefitName}>{category.displayName}</div>
//                             <button className={styles.selectButton}>
//                                 {selectedCategories.includes(category.name) ? '선택됨' : '선택하기'}
//                             </button>
//                         </div>
//                     ))}
//                 </div>
//             </div>
//
//             </tbody>
//             <div className={styles.sidebar}>
//                 <h3>🔍 검색 결과</h3>
//                 <span className={styles.cardCount}>맞춤 카드: {filteredCards.length}개</span>
//                 {selectedCategories.length > 0 && (
//                     <button className={styles.resetButton} onClick={resetSearch}>검색 초기화</button>
//                 )}
//             </div>
//
//             {/*/!* ✅ 필터링된 카드 리스트 *!/*/}
//             {/*<div className={styles.cardList}>*/}
//             {/*    {filteredCards.length > 0 ? (*/}
//             {/*        filteredCards.map((card) => (*/}
//             {/*            <div key={card.id} className={styles.cardItem}>*/}
//             {/*                <h3>{card.name}</h3>*/}
//             {/*                <p>혜택: {card.benefits.join(', ')}</p>*/}
//             {/*            </div>*/}
//             {/*        ))*/}
//             {/*    ) : (*/}
//             {/*        <p className={styles.noCards}>해당하는 카드가 없습니다.</p>*/}
//             {/*    )}*/}
//             {/*</div>*/}
//         </div>
//
//     );
// }

'use client';

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import styles from './styles.module.css';
import {FaTags} from "react-icons/fa";
import Link from "next/link";

// 백엔드 enum과 일치하는 카테고리 타입
type Category = 1 | 2 | 3 | 4 | 5 | 6 | 7;

// 카테고리 정보를 포함하는 객체
interface CategoryInfo {
    name: Category;
    displayName: string; // UI에 표시할 이름
    icon?: React.ReactNode; // 아이콘 (선택 사항)
}

// 카테고리 데이터 (백엔드 enum과 매핑)
const categories: CategoryInfo[] = [
    {name: 1, displayName: "쇼핑", icon: "👕"},
    {name: 2, displayName: "교통", icon: "🚌"},
    {name: 3, displayName: "통신", icon: "📱"},
    {name: 4, displayName: "할인_및_적립", icon: <FaTags/>},
    {name: 5, displayName: "주유", icon: "⛽"},
    {name: 6, displayName: "항공", icon: "✈️"},
    {name: 7, displayName: "음식", icon: "🍔"}
];

export default function selectedBenefit() {
    const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);
    const [totalCardCount, setTotalCardCount] = useState<number>(0);
    const [filteredCardCount, setFilteredCardCount] = useState<number>(0);
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

    // 전체 카드 개수 가져오는 함수
    const fetchTotalCardCount = async () => {
        try {
            const response = await fetch(`api/card_picks/conditions/count`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ categories: [] })
            });
            const count = await response.json();
            setTotalCardCount(count);
            setFilteredCardCount(count); // 초기에는 전체 카드 개수를 필터링된 카드 개수로 설정
        } catch (error) {
            console.error("카드 개수 가져오기 실패:", error);
        }
    };

    // 선택된 혜택에 맞는 카드 개수 가져오는 함수
    const fetchFilteredCards = async (categories: Category[]) => {
        try {
            const response = await fetch(`api/card_picks/conditions/count`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ categories: categories })
            });
            const count = await response.json();
            setFilteredCardCount(count);
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

            // URL 업데이트
            updateURL(updatedCategories);
            fetchFilteredCards(updatedCategories); // 백엔드에서 필터링된 카드 요청

            return updatedCategories;
        });
    };

    // 검색 초기화 버튼
    const resetSearch = () => {
        setSelectedCategories([]);
        updateURL([]);
        fetchTotalCardCount(); // 전체 카드 개수 다시 가져오기
    };

    // URL 업데이트 함수
    const updateURL = (categoryNames: Category[]) => {
        // 기본 URL 구조 설정
        const baseUrl = 'https://www.card-gorilla.com/search/condition';
        const url = new URL(baseUrl);

        // 항상 cate=CRD 파라미터 추가
        url.searchParams.set('cate', 'CRD');

        // 선택된 카테고리가 있으면 search_benefit 파라미터 추가
        if (categoryNames.length > 0) {
            url.searchParams.set('search_benefit', categoryNames.join(','));
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