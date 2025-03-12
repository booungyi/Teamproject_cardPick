"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import styles from "@/styles/Results.module.css";

export default function Results() {
    const router = useRouter();
    const [results, setResults] = useState<string[]>([]);
    const [type, setType] = useState<string>("");

    useEffect(() => {
        const storedAnswers = localStorage.getItem("quizAnswers");
        if (storedAnswers) {
            const answers = JSON.parse(storedAnswers);
            setResults(answers);
            determineType(answers);
        } else {
            router.push("/quiz");
        }
    }, [router]);

    const determineType = (answers: string[]) => {
        const frequency: { [key: string]: number } = {};
        answers.forEach(answer => {
            frequency[answer] = (frequency[answer] || 0) + 1;
        });

        const sortedTypes = Object.keys(frequency).sort((a, b) => frequency[b] - frequency[a]);
        setType(sortedTypes[0]);
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>결과 페이지</h1>
            <p className={styles.description}>당신의 답변에 따른 결과입니다:</p>
            <ul className={styles.resultsList}>
                {results.map((result, index) => (
                    <li key={index} className={styles.resultItem}>
                        {result}
                    </li>
                ))}
            </ul>
            <p className={styles.typeResult}>당신의 유형: {type}</p>
            <button onClick={() => router.push("/")} className={styles.homeButton}>
                홈으로 돌아가기
            </button>
        </div>
    );
}