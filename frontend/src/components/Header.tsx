"use client";

import { useRouter } from "next/navigation";
import styles from "../app/Header.module.css";
import PopularCards from "@/components/ui/popular-cards";
import { Card } from "@/app/page";

export default function Header({ cards }: { cards: Card[] }) {
  const router = useRouter();

  return (
    <header className={styles.header}>
      <div className={styles.headerContent}>
        {/* 왼쪽: 인기카드 */}
        <div className={styles.leftSection}>
          <PopularCards cards={cards} />
        </div>

        {/* 오른쪽: 로고 및 설명 */}
        <div className={styles.logo} onClick={() => router.push("/")}>
          <h1 className={styles.title}>💳 Card Picker</h1>
          <p className={styles.subtitle}>
            나에게 딱 맞는 카드를 쉽고 빠르게 찾아드립니다
          </p>
        </div>
      </div>
    </header>
  );
}
