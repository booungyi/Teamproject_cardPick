"use client";

import styles from "../styles/Home.module.css";
import Image from "next/image";
import { useRouter } from "next/navigation";
import Banner from "@/components/Banner";
import PopularCards from "@/components/ui/popular-cards";
import { useEffect, useState } from "react";

export interface Card {
  id: number;
  cardName: string;
  imageUrl: string;
  detailUrl: string;
  isAdCard: boolean;
  clickCount: number;
}

export default function Home() {
  const router = useRouter();
  const [cards, setCards] = useState<Card[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/card_picks/popular")
      .then((res) => res.json())
      .then((data) => {
        console.log("Fetched ads:", data); // 디버깅용
        setCards(data);
      })
      .catch((err) => console.error("Failed to fetch ads:", err));
  }, []);

  return (
    <>
      <div className={styles.container}>
        <Banner />
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
            <p className={styles.optionDesc}>
              소비성향으로 알아보는
              <br />
              나에게 맞는 카드
            </p>
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
            <p className={styles.optionDesc}>
              상세혜택으로
              <br />
              원하는 혜택 카드 찾기
            </p>
            <button
              onClick={() => router.push("/selectedBenefit")}
              className={styles.cardButton}
            >
              맞춤 카드 검색
            </button>
          </div>
        </div>
        <div>
          <PopularCards cards={cards} />
        </div>
      </div>
    </>
  );
}
