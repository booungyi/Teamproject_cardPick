// components/LoadingScreen.js
"use client";

import { useState, useEffect } from "react";
import styles from "./LoadingScreen.module.css";

const LoadingScreen = ({
  message = "로딩 중입니다...",
  duration = 2000,
  onComplete,
}) => {
  const [dots, setDots] = useState(".");
  const [progress, setProgress] = useState(0);

  // 로딩 애니메이션을 위한 점 애니메이션
  useEffect(() => {
    const dotInterval = setInterval(() => {
      setDots((prev) => {
        if (prev === "...") return ".";
        return prev + ".";
      });
    }, 400);

    return () => clearInterval(dotInterval);
  }, []);

  // 진행 바 애니메이션
  useEffect(() => {
    const startTime = Date.now();
    const timer = setInterval(() => {
      const elapsed = Date.now() - startTime;
      const newProgress = Math.min((elapsed / duration) * 100, 100);
      setProgress(newProgress);

      if (elapsed >= duration && onComplete) {
        clearInterval(timer);
        onComplete();
      }
    }, 50);

    return () => clearInterval(timer);
  }, [duration, onComplete]);

  return (
    <div className={styles.loadingContainer}>
      <div className={styles.loadingContent}>
        <div className={styles.spinner}>
          <div className={styles.spinnerCircle}></div>
        </div>
        <h2 className={styles.loadingMessage}>
          {message}
          <span className={styles.loadingDots}>{dots}</span>
        </h2>
        <div className={styles.progressBarContainer}>
          <div
            className={styles.progressBar}
            style={{ width: `${progress}%` }}
          ></div>
        </div>
      </div>
    </div>
  );
};

export default LoadingScreen;
