// "use client";
//
// import { useState } from "react";
// import { useRouter } from "next/navigation";
// import styles from "../../styles/Quiz.module.css";
//
// const questions = [
//     {
//         id: 1,
//         question: "한 달 예산을 짜는 당신!",
//         options: [
//             { text: "한 달 예산을 철저하게 짜고 그 안에서 소비한다.", value: "budget_planner" },
//             { text: "쓰고 싶은 대로 쓰고 나중에 결산하는 편이다.", value: "free_spender" }
//         ]
//     },
//     {
//         id: 2,
//         question: "카드 선택, 당신의 기준은?",
//         options: [
//             { text: "카드 혜택을 꼼꼼하게 따져보고 최고의 카드를 고른다.", value: "benefit_seeker" },
//             { text: "그냥 인기 있는 카드, 디자인이 예쁜 카드가 좋다.", value: "design_lover" }
//         ]
//     }
// ];
//
// export default function Quiz() {
//     const [step, setStep] = useState(0);
//     const [answers, setAnswers] = useState({});
//     const router = useRouter();
//
//     const handleAnswer = (value) => {
//         if (typeof value !== "string") {
//             console.error("Invalid answer value (not a string):", value);
//             return;
//         }
//         if (step < 0 || step >= questions.length || !questions[step]) {
//             console.error("Invalid step or question out of bounds:", step);
//             return;
//         }
//
//         setAnswers((prev) => ({ ...prev, [questions[step].id]: value }));
//
//         if (step < questions.length - 1) {
//             setStep(step + 1);
//         } else {
//             console.log("최종 결과:", { ...answers, [questions[step].id]: value });
//             router.push("/results");
//         }
//     };
//
//     return (
//         <div className={styles.container}>
//             {questions[step] ? (
//                 <>
//                     <h2 className={styles.question}>{questions[step].question}</h2>
//                     <div className={styles.options}>
//                         {questions[step].options.map((option, index) => (
//                             <button
//                                 key={index}
//                                 className={styles.optionButton}
//                                 onClick={() => handleAnswer(option.value)}
//                             >
//                                 {option.text}
//                             </button>
//                         ))}
//                     </div>
//                     <div className={styles.progressBarContainer}>
//                         <div className={styles.progressBar} style={{ width: `${((step + 1) / questions.length) * 100}%` }}></div>
//                     </div>
//                     {step > 0 && (
//                         <button className={styles.backButton} onClick={() => setStep(step - 1)}>
//                             이전으로
//                         </button>
//                     )}
//                 </>
//             ) : (
//                 <p className="text-red-500">질문을 불러오는 중 오류가 발생했습니다.</p>
//             )}
//         </div>
//     );
// }
