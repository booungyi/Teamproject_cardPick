"use client";

import styles from "../styles/Home.module.css";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function Home() {
    const router = useRouter();

    return (
        <>
            <div className={styles.container}>
                <h1 className={styles.title}>💳 카드픽커</h1>
                <p className={styles.subtitle}>나에게 딱 맞는 카드를 쉽고 빠르게 찾아드립니다</p>

                <div className={styles.optionsContainer}>
                    <div className={styles.optionCard}>
                        <div className={styles.iconWrapper}>
                            <Image
                                src="/icons/test-icon.svg"
                                alt="테스트 아이콘"
                                width={64}
                                height={64}
                            />
                            <span className={styles.timerBadge}>1분</span>
                        </div>
                        <h3 className={styles.optionTitle}>1분 테스트로 추천 받기</h3>
                        <p className={styles.optionDesc}>소비성향으로 알아보는<br />나에게 맞는 카드</p>
                        <button
                            onClick={() => router.push("/quiz")}
                            className={styles.cardButton}
                        >
                            카드추천 테스트
                        </button>
                    </div>

                    <div className={styles.optionCard}>
                        <div className={styles.iconWrapper}>
                            <Image
                                src="/icons/search-icon.svg"
                                alt="검색 아이콘"
                                width={64}
                                height={64}
                            />
                        </div>
                        <h3 className={styles.optionTitle}>맞춤 혜택으로 직접 찾기</h3>
                        <p className={styles.optionDesc}>상세혜택으로<br />원하는 혜택 카드 찾기</p>
                        <button
                            onClick={() => router.push("/selectedBenefit")}
                            className={styles.cardButton}
                        >
                            맞춤 카드 검색
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
}
