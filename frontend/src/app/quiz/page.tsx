"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import styles from "@/styles/Quiz.module.css";

const questions = [
    {
        id: 1,
        question: "💰 한 달 예산을 어떻게 관리하나요?",
        options: [
            { text: "📊 철저하게 예산을 짜고 그 안에서 소비해요", value: "J" },
            { text: "🛍️ 쓰고 싶은 대로 쓰고 나중에 결산하는 편이에요", value: "P" }
        ]
    },
    {
        id: 2,
        question: "🎨 카드 선택 시 가장 중요한 기준은?",
        options: [
            { text: "💡 혜택을 꼼꼼히 따지고 분석해요", value: "T" },
            { text: "💖 디자인이나 감성적인 요소가 중요해요", value: "F" }
        ]
    },
    {
        id: 3,
        question: "💳 카드 사용 패턴은 어떤가요?",
        options: [
            { text: "🔥 한 가지 카드만 집중적으로 사용해요", value: "S" },
            { text: "🎭 여러 카드를 사용하며 혜택을 최대로 활용해요", value: "N" }
        ]
    },
    {
        id: 4,
        question: "📉 할인 혜택을 받을 때 어떤 방식을 선호하나요?",
        options: [
            { text: "🎯 한 가지 영역에서 최대 할인을 받는 것이 좋아요", value: "J" },
            { text: "🌍 다양한 영역에서 조금씩 할인받는 게 좋아요", value: "P" }
        ]
    },
    {
        id: 5,
        question: "💵 신용카드 사용 습관은?",
        options: [
            { text: "💳 신용카드를 자주 사용하고 할부도 적극 활용해요", value: "T" },
            { text: "💰 신용카드를 가끔 사용하고 주로 체크카드를 이용해요", value: "F" }
        ]
    },
    {
        id: 6,
        question: "💡 신용카드의 혜택을 어떻게 활용하나요?",
        options: [
            { text: "🛒 생활 속에서 꾸준히 할인을 받아요", value: "S" },
            { text: "🏡 큰 비용이 들어가는 결제에서 할부 혜택을 누려요", value: "N" }
        ]
    }
];

export default function Quiz() {
    const [step, setStep] = useState(0);
    const [answers, setAnswers] = useState<string[]>([]);
    const router = useRouter();

    const handleAnswer = (value: string) => {
        setAnswers((prev) => [...prev, value]);
        if (step < questions.length - 1) {
            setStep(step + 1);
        } else {
            console.log("최종 답변: ", [...answers, value]);
            router.push("/results"); // 결과 페이지로 이동
        }
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.question}>{questions[step].question}</h2>
            <div className={styles.options}>
                {questions[step].options.map((option, index) => (
                    <button
                        key={index}
                        onClick={() => handleAnswer(option.value)}
                        className={styles.optionButton}
                    >
                        {option.text}
                    </button>
                ))}
            </div>
            <div className={styles.progressBarContainer}>
                <div
                    className={styles.progressBar}
                    style={{ width: `${((step + 1) / questions.length) * 100}%` }}
                ></div>
            </div>
            {step > 0 && (
                <button
                    onClick={() => setStep(step - 1)}
                    className={styles.backButton}
                >
                    이전 질문으로 돌아가기
                </button>
            )}
        </div>
    );
}