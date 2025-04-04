"use client";

import { useRouter } from "next/navigation";
import Link from "next/link";
import styles from "../app/Header.module.css";

export default function Header() {
  const router = useRouter();

  return (
    <header className={styles.header}>
      <div className={styles.headerContent}>
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
