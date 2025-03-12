'use client';

import {useEffect, useState} from "react";
import styles from './selectedBenefit.module.css';
import { FaShoppingBag, FaBus, FaMobileAlt, FaGasPump, FaPlane, FaUtensils, FaTags } from "react-icons/fa";

// 백엔드 enum과 일치하는 카테고리 타입
type Category = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7;

// 카테고리 정보를 포함하는 객체
interface CategoryInfo {
    name: Category;
    displayName: string; // UI에 표시할 이름
    icon?: React.ReactNode; // 아이콘 (선택 사항)
}

// 카테고리 데이터 (백엔드 enum과 매핑)
const categories: CategoryInfo[] = [
    { name: 0, displayName: "쇼핑", icon: "👕" },
    { name: 1, displayName: "교통", icon: "🚌" },
    { name: 2, displayName: "통신", icon: "📱" },
    { name: 3, displayName: "할인_및_적립", icon: <FaTags /> },
    { name: 4, displayName: "주유", icon: "⛽" },
    { name: 5, displayName: "항공", icon: "✈️" },
    { name: 6, displayName: "음식", icon: "🍔" },
    { name: 7, displayName: "기타" },
];

export default function selectedBenefit() {
    const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);

    // 컴포넌트가 마운트될 때 URL에서 이미 선택된 카테고리가 있는지 확인
    useEffect(() => {
        const url = new URL(window.location.href);
        const searchBenefitParam = url.searchParams.get('search_benefit');

        if (searchBenefitParam) {
            const categoryNames = searchBenefitParam.split(',').map(Number) as Category[];
            setSelectedCategories(categoryNames);
        }
    }, []);

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

            return updatedCategories;
        });
    };

    // URL 업데이트 함수
    const updateURL = (categoryNames: Category[]) => {
        const url = new URL(window.location.href);
        url.searchParams.set('cate', 'CRD');

        if (categoryNames.length > 0) {
            url.searchParams.set('search_benefit', categoryNames.join(','));
        } else {
            url.searchParams.delete('search_benefit');
        }

        // 페이지 리로드 없이 URL 업데이트
        window.history.pushState(null, '', url.toString());
        // window.history.pushState({ path: url.toString() }, '', url.toString());
    };

    return (
        <div className={styles.container}>
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
    );
}