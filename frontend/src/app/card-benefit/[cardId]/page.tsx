"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import styles from "./CardBenefitPage.module.css";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import { FaTags } from "react-icons/fa";

interface Benefit {
  id: number;
  benefitName: string;
  benefitDetail: string;
}

interface CardBenefitData {
  cardName: string;
  cardImageUrl: string;
  applyLink: string;
  benefits: Benefit[];
}

type Category =
  | "쇼핑"
  | "교통"
  | "통신"
  | "할인_및_적립"
  | "주유"
  | "항공"
  | "음식";

interface CategoryInfo {
  name: Category;
  displayName: string;
  icon?: React.ReactNode;
}
const categories: CategoryInfo[] = [
  { name: "쇼핑", displayName: "쇼핑", icon: "👕" },
  { name: "교통", displayName: "교통", icon: "🚌" },
  { name: "통신", displayName: "통신", icon: "📱" },
  { name: "할인_및_적립", displayName: "할인 및 적립", icon: <FaTags /> },
  { name: "주유", displayName: "주유", icon: "⛽" },
  { name: "항공", displayName: "항공", icon: "✈️" },
  { name: "음식", displayName: "음식", icon: "🍔" },
];

export default function CardBenefitPage() {
  const { cardId } = useParams();

  const [data, setData] = useState<CardBenefitData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [openBenefit, setOpenBenefit] = useState<number | null>(null);

  useEffect(() => {
    if (!cardId) return;

    fetch(`http://localhost:8080/api/card-benefits/${cardId}`)
      .then((res) => (res.ok ? res.json() : Promise.reject("Failed to fetch")))
      .then(setData)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [cardId]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>에러 발생: {error}</p>;

  return (
    <div className={styles.container}>
      <div className={styles.cardHeader}>
        <img
          src={data?.cardImageUrl}
          alt={data?.cardName}
          className={styles.cardImage}
        />
        <div className={styles.cardInfo}>
          <h1>{data?.cardName}</h1>
          <button
            onClick={() => {
              if (data?.applyLink) {
                location.href = data.applyLink;
              } else {
                alert("신청 링크가 없습니다.");
              }
            }}
            className={styles.applyButton}
          >
            카드 신청
          </button>
        </div>
      </div>
      <div className={styles.benefitsSection}>
        <h2>주요 혜택</h2>
        <Accordion type="single" collapsible>
          {data?.benefits.map((benefit, index) => (
            <AccordionItem
              key={index}
              className={styles.benefit_layout}
              value={`item-${index}`}
            >
              <AccordionTrigger className={styles.benefit_name}>
                {benefit.benefitName}
              </AccordionTrigger>
              <AccordionContent className={styles.benefit_detail}>
                {benefit.benefitDetail}
              </AccordionContent>
            </AccordionItem>
          ))}
        </Accordion>
      </div>
    </div>
  );
}
