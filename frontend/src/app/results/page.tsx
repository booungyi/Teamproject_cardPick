"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import Image from "next/image";
import styles from "@/styles/Results.module.css";

// 카드 정보 인터페이스
interface Card {
    id: string;
    name: string;
    description?: string;
    imageUrl: string;
    hasEvent: boolean;
}

// 카테고리 정보 인터페이스
interface CategoryInfo {
    title: string;
    benefits: string[];
    cards: Card[];
}

// 유형 타입 정의
type UserType = 'J' | 'P' | 'T' | 'F' | 'S' | 'N';

// 유형별 타이틀 매핑
const typeTitles: Record<UserType, string> = {
    J: "계획적인 소비를 위한 J유형 추천 카드",
    P: "유연한 소비를 위한 P유형 추천 카드",
    T: "혜택 분석에 강한 T유형 추천 카드",
    F: "감성적 가치를 중시하는 F유형 추천 카드",
    S: "안정적인 혜택의 S유형 추천 카드",
    N: "다양한 혜택을 활용하는 N유형 추천 카드"
};

// 유형별 혜택 포인트 매핑
const typeBenefits: Record<UserType, string[]> = {
    J: [
        "계획적인 예산 관리를 선호하는 분들께 적합해요",
        "한도없이 무한대로 적립하고 싶어요"
    ],
    P: [
        "유연하게 예산을 활용하는 분들께 적합해요",
        "마일리지로 유럽여행 비즈니스 도전해보고 싶어요"
    ],
    T: [
        "혜택을 꼼꼼히 따지고 분석하는 분들께 적합해요",
        "한도없이 무한대로 적립하고 싶어요"
    ],
    F: [
        "디자인과 감성적 가치를 중시하는 분들께 적합해요",
        "마일리지로 유럽여행 비즈니스 도전해보고 싶어요"
    ],
    S: [
        "한 가지 카드를 집중적으로 사용하는 분들께 적합해요",
        "한도없이 무한대로 적립하고 싶어요"
    ],
    N: [
        "여러 카드의 혜택을 최대한 활용하는 분들께 적합해요",
        "마일리지로 유럽여행 비즈니스 도전해보고 싶어요"
    ]
};

// 백엔드가 완성되기 전에 사용할 임시 데이터
const mockCardData: Record<UserType, CategoryInfo> = {
    J: {
        title: "계획적인 소비를 위한 J유형 추천 카드",
        benefits: typeBenefits.J,
        cards: [
            {
                id: "j1",
                name: "삼성카드 & MILEAGE PLATINUM",
                description: "(스카이패스)",
                imageUrl: "/images/cards/samsung-mileage.jpg",
                hasEvent: true
            },
            {
                id: "j2",
                name: "카드의정석 EVERY MILE SKYPASS",
                description: "",
                imageUrl: "/images/cards/every-mile.jpg",
                hasEvent: true
            },
            {
                id: "j3",
                name: "신한카드 Air 1.5",
                description: "",
                imageUrl: "/images/cards/shinhan-air.jpg",
                hasEvent: true
            },
            {
                id: "j4",
                name: "I-Mileage",
                description: "(대한항공)",
                imageUrl: "/images/cards/i-mileage.jpg",
                hasEvent: false
            }
        ]
    },
    P: {
        title: "유연한 소비를 위한 P유형 추천 카드",
        benefits: typeBenefits.P,
        cards: [
            {
                id: "p1",
                name: "신한카드 Air 1.5",
                description: "",
                imageUrl: "/images/cards/shinhan-air.jpg",
                hasEvent: true
            },
            {
                id: "p2",
                name: "I-Mileage",
                description: "(대한항공)",
                imageUrl: "/images/cards/i-mileage.jpg",
                hasEvent: false
            },
            {
                id: "p3",
                name: "삼성카드 & MILEAGE PLATINUM",
                description: "(스카이패스)",
                imageUrl: "/images/cards/samsung-mileage.jpg",
                hasEvent: true
            },
            {
                id: "p4",
                name: "하나 스카이패스",
                description: "아멕스 플래티늄카드",
                imageUrl: "/images/cards/hana-skypass.jpg",
                hasEvent: false
            }
        ]
    },
    T: {
        title: "혜택 분석에 강한 T유형 추천 카드",
        benefits: typeBenefits.T,
        cards: [
            {
                id: "t1",
                name: "카드의정석 EVERY MILE SKYPASS",
                description: "",
                imageUrl: "/images/cards/every-mile.jpg",
                hasEvent: true
            },
            {
                id: "t2",
                name: "신한카드 Air 1.5",
                description: "",
                imageUrl: "/images/cards/shinhan-air.jpg",
                hasEvent: true
            },
            {
                id: "t3",
                name: "I-Mileage",
                description: "(대한항공)",
                imageUrl: "/images/cards/i-mileage.jpg",
                hasEvent: false
            },
            {
                id: "t4",
                name: "하나 스카이패스",
                description: "아멕스 플래티늄카드",
                imageUrl: "/images/cards/hana-skypass.jpg",
                hasEvent: false
            }
        ]
    },
    F: {
        title: "감성적 가치를 중시하는 F유형 추천 카드",
        benefits: typeBenefits.F,
        cards: [
            {
                id: "f1",
                name: "I-Mileage",
                description: "(대한항공)",
                imageUrl: "/images/cards/i-mileage.jpg",
                hasEvent: false
            },
            {
                id: "f2",
                name: "하나 스카이패스",
                description: "아멕스 플래티늄카드",
                imageUrl: "/images/cards/hana-skypass.jpg",
                hasEvent: false
            },
            {
                id: "f3",
                name: "삼성카드 & MILEAGE PLATINUM",
                description: "(스카이패스)",
                imageUrl: "/images/cards/samsung-mileage.jpg",
                hasEvent: true
            },
            {
                id: "f4",
                name: "신한카드 Air 1.5",
                description: "",
                imageUrl: "/images/cards/shinhan-air.jpg",
                hasEvent: true
            }
        ]
    },
    S: {
        title: "안정적인 혜택의 S유형 추천 카드",
        benefits: typeBenefits.S,
        cards: [
            {
                id: "s1",
                name: "하나 스카이패스",
                description: "아멕스 플래티늄카드",
                imageUrl: "/images/cards/hana-skypass.jpg",
                hasEvent: false
            },
            {
                id: "s2",
                name: "삼성카드 & MILEAGE PLATINUM",
                description: "(스카이패스)",
                imageUrl: "/images/cards/samsung-mileage.jpg",
                hasEvent: true
            },
            {
                id: "s3",
                name: "I-Mileage",
                description: "(대한항공)",
                imageUrl: "/images/cards/i-mileage.jpg",
                hasEvent: false
            },
            {
                id: "s4",
                name: "카드의정석 EVERY MILE SKYPASS",
                description: "",
                imageUrl: "/images/cards/every-mile.jpg",
                hasEvent: true
            }
        ]
    },
    N: {
        title: "다양한 혜택을 활용하는 N유형 추천 카드",
        benefits: typeBenefits.N,
        cards: [
            {
                id: "n1",
                name: "신한카드 Air 1.5",
                description: "",
                imageUrl: "/images/cards/shinhan-air.jpg",
                hasEvent: true
            },
            {
                id: "n2",
                name: "카드의정석 EVERY MILE SKYPASS",
                description: "",
                imageUrl: "/images/cards/every-mile.jpg",
                hasEvent: true
            },
            {
                id: "n3",
                name: "I-Mileage",
                description: "(대한항공)",
                imageUrl: "/images/cards/i-mileage.jpg",
                hasEvent: false
            },
            {
                id: "n4",
                name: "하나 스카이패스",
                description: "아멕스 플래티늄카드",
                imageUrl: "/images/cards/hana-skypass.jpg",
                hasEvent: false
            }
        ]
    }
};

export default function Results() {
    const router = useRouter();
    const [results, setResults] = useState<string[]>([]);
    const [type, setType] = useState<UserType | "">("");
    const [categoryInfo, setCategoryInfo] = useState<CategoryInfo | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedAnswers = localStorage.getItem("quizAnswers");
        if (storedAnswers) {
            const answers = JSON.parse(storedAnswers);
            setResults(answers);
            determineType(answers);
        } else {
            // 퀴즈 결과가 없으면 퀴즈 페이지로 리다이렉트
            router.push("/quiz");
        }
    }, [router]);

    const determineType = (answers: string[]) => {
        const frequency: { [key: string]: number } = {};
        answers.forEach(answer => {
            frequency[answer] = (frequency[answer] || 0) + 1;
        });

        const sortedTypes = Object.keys(frequency).sort((a, b) => frequency[b] - frequency[a]);
        const userType = sortedTypes[0] as UserType;
        setType(userType);

        // 백엔드 연결 대신 임시 데이터 사용
        fetchMockCardsByUserType(userType);
    };

    const fetchMockCardsByUserType = (userType: UserType) => {
        // 로딩 상태 설정
        setLoading(true);

        // 백엔드 API 호출을 시뮬레이션
        setTimeout(() => {
            // 해당 유형에 맞는 목업 데이터 설정
            if (userType && Object.keys(mockCardData).includes(userType)) {
                setCategoryInfo(mockCardData[userType]);
            } else {
                // 해당 유형이 없으면 퀴즈 페이지로 리다이렉트
                router.push("/quiz");
            }
            setLoading(false);
        }, 800); // 0.8초 지연으로 로딩 효과 추가
    };

    // 실제 백엔드 연결 시 사용할 함수 (나중에 활성화)
    const fetchCardsByUserType = async (userType: UserType) => {
        try {
            setLoading(true);
            const response = await fetch(`/api/card-recommendations?type=${userType}`);
            const data = await response.json();
            setCategoryInfo(data);
        } catch (error) {
            console.error("카드 정보를 가져오는데 실패했습니다:", error);
            // 에러 발생 시 퀴즈 페이지로 리다이렉트
            router.push("/quiz");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <div className={styles.loading}>카드 정보를 불러오는 중...</div>;
    }

    return (
        <div className={styles.container}>
            {categoryInfo && (
                <div className={styles.recommendationSection}>
                    {/* 유형에 따라 다른 제목을 표시 */}
                    <h1 className={styles.mainTitle}>
                        {(type && typeTitles[type as UserType]) || "마일리지 빠르게 쌓아볼 픽커님께 추천"}
                    </h1>

                    <div className={styles.benefitPoints}>
                        {/* 유형에 따른 혜택 포인트 표시 */}
                        {(type && typeBenefits[type as UserType]?.map((benefit, index) => (
                                <div key={index} className={styles.benefitItem}>
                                    <span className={styles.checkmark}>✓</span>
                                    <span>{benefit}</span>
                                </div>
                            ))) ||
                            /* 백엔드에서 받은 정보 사용 */
                            categoryInfo.benefits.map((benefit, index) => (
                                <div key={index} className={styles.benefitItem}>
                                    <span className={styles.checkmark}>✓</span>
                                    <span>{benefit}</span>
                                </div>
                            ))}
                    </div>

                    <div className={styles.cardContainer}>
                        {categoryInfo.cards.map((card) => (
                            <div key={card.id} className={styles.cardItem}>
                                <div className={styles.cardImageWrapper}>
                                    {card.hasEvent && (
                                        <div className={styles.eventTag}>
                                            이벤트 진행중
                                        </div>
                                    )}
                                    <div className={styles.cardImage}>
                                        <Image
                                            src={card.imageUrl}
                                            alt={card.name}
                                            fill
                                            style={{objectFit: 'contain'}}
                                            onError={(e) => {
                                                // 이미지 로드 실패 시 기본 이미지로 대체
                                                const target = e.target as HTMLImageElement;
                                                target.onerror = null;
                                                target.src = '/images/card-placeholder.jpg';
                                            }}
                                        />
                                    </div>
                                </div>
                                <h3 className={styles.cardName}>{card.name}</h3>
                                {card.description && (
                                    <p className={styles.cardDescription}>{card.description}</p>
                                )}
                            </div>
                        ))}
                    </div>
                </div>
            )}

            <button onClick={() => router.push("/")} className={styles.homeButton}>
                홈으로 돌아가기
            </button>
        </div>
    );
}
