"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import styles from "../app/Header.module.css";
import PopularCards from "@/components/ui/popular-cards";


export interface Card {
  id: number;
  cardName: string;
  imageUrl: string;
  detailUrl: string;
  isAdCard: boolean;
  clickCount: number;
}

export default function Header() {
  const router = useRouter();

  const [cards, setCards] = useState<Card[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/card_picks/popular")
      .then((res) => res.json())
      .then((data) => setCards(data))
      .catch((err) => console.error("Failed to fetch cards:", err));
  }, []);

  return (
    <header className={styles.header}>
      <div className={styles.headerContent}>
        <div className={styles.logo}>
          <PopularCards cards={cards} />
          <h1 className={styles.title} onClick={() => router.push("/")}>
            💳 Card Picker
          </h1>
          <p className={styles.subtitle}>
            나에게 딱 맞는 카드를 쉽고 빠르게 찾아드립니다
          </p>
        </div>
      </div>
    </header>
  );
}
