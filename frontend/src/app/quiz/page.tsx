"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import styles from "@/styles/Quiz.module.css";
import LoadingScreen from "@/components/LoadingScreen"; // 로딩 컴포넌트 import
import { AnimatePresence, motion } from "framer-motion";

const questions = [
  {
    id: 1,
    question: "💰 한 달 예산을 어떻게 관리하나요?",
    options: [
      { text: "📊 철저하게 예산을 짜고 그 안에서 소비해요", value: "J" },
      { text: "🛍️ 쓰고 싶은 대로 쓰고 나중에 결산하는 편이에요", value: "P" },
      { text: "💡 혜택을 꼼꼼히 따지고 분석해요", value: "T" },
    ],
  },
  {
    id: 2,
    question: "🎨 카드 선택 시 가장 중요한 기준은?",
    options: [
      { text: "💡 혜택을 꼼꼼히 따지고 분석해요", value: "T" },
      { text: "💖 디자인이나 감성적인 요소가 중요해요", value: "F" },
      { text: "🔥 한 가지 카드만 집중적으로 사용해요", value: "S" },
    ],
  },
  {
    id: 3,
    question: "💳 카드 사용 패턴은 어떤가요?",
    options: [
      { text: "🔥 한 가지 카드만 집중적으로 사용해요", value: "S" },
      { text: "🎭 여러 카드를 사용하며 혜택을 최대로 활용해요", value: "N" },
      {
        text: "💰 신용카드를 가끔 사용하고 주로 체크카드를 이용해요",
        value: "F",
      },
    ],
  },
  {
    id: 4,
    question: "📉 할인 혜택을 받을 때 어떤 방식을 선호하나요?",
    options: [
      { text: "🎯 한 가지 영역에서 최대 할인을 받는 것이 좋아요", value: "J" },
      { text: "🌍 다양한 영역에서 조금씩 할인받는 게 좋아요", value: "P" },
      { text: "🛒 생활 속에서 꾸준히 할인을 받아요", value: "S" },
    ],
  },
  {
    id: 5,
    question: "💵 신용카드 사용 습관은?",
    options: [
      { text: "💳 신용카드를 자주 사용하고 할부도 적극 활용해요", value: "T" },
      {
        text: "💰 신용카드를 가끔 사용하고 주로 체크카드를 이용해요",
        value: "F",
      },
      { text: "🏡 큰 비용이 들어가는 결제에서 할부 혜택을 누려요", value: "N" },
    ],
  },
  {
    id: 6,
    question: "🛍️ 쇼핑할 때 어떤 방식을 선호하나요?",
    options: [
      { text: "🛒 생활 속에서 꾸준히 할인을 받아요", value: "S" },
      { text: "🏡 큰 비용이 들어가는 결제에서 할부 혜택을 누려요", value: "N" },
      { text: "📊 철저하게 예산을 짜고 그 안에서 소비해요", value: "J" },
    ],
  },
];

export default function Quiz() {
  const [step, setStep] = useState(0);
  const [answers, setAnswers] = useState([]);
  const [slideDirection, setSlideDirection] = useState("");
  const [isAnimating, setIsAnimating] = useState(false);
  const [progressWidth, setProgressWidth] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  // 컴포넌트 마운트 시 로컬 스토리지 초기화
  useEffect(() => {
    localStorage.removeItem("quizAnswers");

    // 컴포넌트 마운트 시 오른쪽에서 슬라이드되어 들어오는 첫 질문
    setTimeout(() => {
      setSlideDirection("slide-in-right");
    }, 100);
  }, []);

  useEffect(() => {
    // Update progress bar
    const newWidth = (step / (questions.length - 1)) * 100;
    setProgressWidth(newWidth);
  }, [step]);

  const handleAnswer = (value) => {
    if (isAnimating) return;

    setIsAnimating(true);
    setSlideDirection("slide-out-left"); // 현재 질문이 왼쪽으로 나가는 애니메이션

    // 현재 선택한 답변을 포함한 새 배열 생성
    const newAnswers = [...answers, value];

    setTimeout(() => {
      setAnswers(newAnswers);

      if (step < questions.length - 1) {
        setStep(step + 1);
        // 새 질문이 오른쪽에서 들어오는 애니메이션
        setTimeout(() => {
          setSlideDirection("slide-in-right");
          setIsAnimating(false);
        }, 50);
      } else {
        // 마지막 질문 후 로딩 화면 표시
        setIsLoading(true);
        console.log("최종 답변: ", newAnswers);
        localStorage.setItem("quizAnswers", JSON.stringify(newAnswers));

        // 로딩 화면 표시 후 결과 페이지로 이동
        setTimeout(() => {
          try {
            router.push("/quiz/results");
          } catch (error) {
            console.error("라우팅 에러:", error);
            window.location.href = "/quiz/results";
          }
        }, 2000); // 로딩 시간을 2초로 설정
      }
    }, 300);
  };

  const handleBack = () => {
    if (isAnimating || step === 0) return;

    setIsAnimating(true);
    setSlideDirection("slide-out-right"); // 현재 질문이 오른쪽으로 나가는 애니메이션

    setTimeout(() => {
      // 이전 단계로 이동할 때 마지막 답변 제거
      setAnswers((prev) => prev.slice(0, -1));
      setStep(step - 1);

      // 이전 질문이 왼쪽에서 들어오는 애니메이션
      setTimeout(() => {
        setSlideDirection("slide-in-left");
        setIsAnimating(false);
      }, 50);
    }, 300);
  };

  // 로딩 중이면 로딩 컴포넌트 표시
  if (isLoading) {
    return <LoadingScreen message="결과를 분석하는 중..." />;
  }

  return (
    <div className={styles.container}>
      <div className={styles.quizHeader}>
        <h1 className={styles.quizTitle}>카드 성향 테스트</h1>
        <p className={styles.quizDescription}>
          나에게 맞는 신용카드 스타일을 알아보세요
        </p>
      </div>

      {/*<div className={styles.questionContainer}>*/}
      <div className={styles.questionContainer}>
        <AnimatePresence mode="wait">
          <motion.div
            key={step}
            initial={{ x: 200, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            exit={{ x: -200, opacity: 0 }}
            transition={{ duration: 0.4, ease: "easeInOut" }}
            className={styles.question}
          >
            {questions[step].question}
          </motion.div>
        </AnimatePresence>
      </div>

      {/*</div>*/}

      <div className={styles.options}>
        {questions[step].options.map((option, index) => (
          <motion.button
            key={index}
            onClick={() => handleAnswer(option.value)}
            className={styles.optionButton}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 + index * 0.1 }}
            disabled={isAnimating}
          >
            {option.text}
          </motion.button>
        ))}
      </div>

      <div className={styles.progressBarContainer}>
        <div
          className={styles.progressBar}
          style={{
            width: `${progressWidth}%`,
            transition: "width 0.5s ease-in-out",
          }}
        ></div>
      </div>

      <div className={styles.navigationControls}>
        {step > 0 && (
          <button
            onClick={handleBack}
            className={styles.backButton}
            disabled={isAnimating}
          >
            이전 질문으로 돌아가기
          </button>
        )}

        <div className={styles.pageIndicator}>
          {`page ${step + 1} / ${questions.length}`}
        </div>
      </div>
    </div>
  );
}
