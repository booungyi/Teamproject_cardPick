"use client";

import { useRouter } from "next/navigation";
import styles from "../styles/Home.module.css"; // ✅ 상대 경로로 수정

export default function Home() {
    const router = useRouter();

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>💳 나에게 딱 맞는 카드를 찾아보세요!</h1>
            <p className={styles.description}>간단한 테스트를 통해 맞춤 카드를 추천해드립니다.</p>

            <button
                onClick={() => router.push("/quiz")}
                className={styles.button}
            >
                테스트 시작하기 🚀
            </button>
        </div>
    );
}