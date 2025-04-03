"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import styles from "./styles.module.css";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import {
  FaTags,
  FaShoppingBag,
  FaBus,
  FaMobile,
  FaGasPump,
  FaPlane,
  FaHamburger,
  FaGlobe,
  FaExclamationTriangle,
  FaChevronDown,
} from "react-icons/fa";

interface Benefit {
  id: number;
  benefitName: string;
  benefitDetail: string;
  category: Category;
  subTitle?: string;
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
  | "영화"
  | "해외"
  | "유의사항";

const getCategoryIcon = (category: Category) => {
  switch (category) {
    case "쇼핑":
      return <FaShoppingBag />;
    case "교통":
      return <FaBus />;
    case "통신":
      return <FaMobile />;
    case "할인_및_적립":
      return <FaTags />;
    case "주유":
      return <FaGasPump />;
    case "항공":
      return <FaPlane />;
    case "영화":
      return "🎬";
    case "해외":
      return <FaGlobe />;
    case "유의사항":
      return <FaExclamationTriangle />;
    default:
      return <FaHamburger />;
  }
};

export default function CardBenefitPage() {
  const { cardId } = useParams();

  // 샘플 데이터 - 실제로는 API에서 가져옵니다
  const [data, setData] = useState<CardBenefitData | null>(/*{
    cardName: "프리미엄 카드",
    cardImageUrl: "https://via.placeholder.com/300x190",
    applyLink: "#",
    benefits: [
      {
        id: 1,
        category: "쇼핑",
        benefitName: "쇼핑",
        subTitle: "라이프스타일 패키지 선택",
        benefitDetail:
          "온라인 쇼핑몰에서 10% 캐시백 혜택을 제공합니다. 최대 월 10,000원까지 적립됩니다.",
      },
      {
        id: 2,
        category: "교통",
        benefitName: "대중교통",
        subTitle: "택시 10% 결제할인",
        benefitDetail:
          "버스, 지하철 이용 시 10% 할인 혜택을 제공합니다. 택시 결제 시에도 10% 할인이 적용됩니다.",
      },
      {
        id: 3,
        category: "통신",
        benefitName: "통신",
        subTitle: "이동통신요금 10% 결제할인",
        benefitDetail:
          "이동통신 요금 결제 시 10% 할인 혜택을 제공합니다. 최대 월 5,000원까지 할인됩니다.",
      },
      {
        id: 4,
        category: "영화",
        benefitName: "영화",
        subTitle: "CGV 및 롯데시네마 5,000원 결제할인",
        benefitDetail:
          "CGV, 롯데시네마에서 결제 시 건당 5,000원 할인 혜택을 제공합니다. 월 최대 2회까지 사용 가능합니다.",
      },
      {
        id: 5,
        category: "해외",
        benefitName: "해외",
        subTitle: "해외 1.3% 적립",
        benefitDetail:
          "해외 결제 시 1.3% 추가 적립 혜택을 제공합니다. 해외 직구, 여행 등 모든 해외 가맹점에 적용됩니다.",
      },
      {
        id: 6,
        category: "유의사항",
        benefitName: "유의사항",
        subTitle: "꼭 확인하세요!",
        benefitDetail:
          "모든 혜택은 전월 실적 50만원 이상 시 제공됩니다. 전월 실적 미달 시 혜택이 적용되지 않습니다. 자세한 내용은 카드사 홈페이지를 참고하세요.",
      },
    ],
  }*/);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!cardId) return;

    // 실제 구현 시에는 아래 코드 사용
    setLoading(true);
    fetch(`http://localhost:8080/api/card-benefits/${cardId}`)
      .then((res) => (res.ok ? res.json() : Promise.reject("Failed to fetch")))
      .then(setData)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [cardId]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>에러 발생: {error}</p>;
  if (!data) return <p>데이터가 없습니다.</p>;

  return (
    <div className={styles.container}>
      <div className={styles.cardInfo}>
        {/* 카드 이미지 (왼쪽) */}
        <img
          src={data.cardImageUrl}
          // alt={data.cardName}
          className={styles.cardImage}
        />

        {/* 카드 정보 (오른쪽) */}
        <div className={styles.cardDetails}>
          <h1 className={styles.cardTitle}>{data.cardName}</h1>
          <p>
            {data.benefits.slice(0, 3).map((benefit, index) => (
              <span key={index} className={styles.benefitText}>
                • {benefit.benefitName}
                <br /> {/* ✅ 줄바꿈 추가 */}
              </span>
            ))}
          </p>
          <button
            className={styles.applyButton}
            onClick={() => {
              if (data.applyLink) {
                window.location.href = data.applyLink;
              } else {
                alert("신청 링크가 없습니다.");
              }
            }}
          >
            카드 신청 바로가기
          </button>
        </div>
      </div>

      <Accordion type="single" collapsible>
        {data.benefits.map((benefit, index) => (
          <AccordionItem
            key={index}
            className={styles.benefit_layout}
            value={`item-${index}`}
          >
            <AccordionTrigger className={styles.benefit_name}>
              <div className={styles.benefitHeader}>
                <div className={styles.benefitIcon}>
                  {getCategoryIcon(benefit.category)}
                </div>
                <div className={styles.benefitTitles}>
                  <span className={styles.benefitMainTitle}>
                    {benefit.benefitName}
                  </span>
                  {benefit.subTitle && (
                    <span className={styles.benefitSubTitle}>
                      {benefit.subTitle}
                    </span>
                  )}
                </div>
              </div>
              {/*<FaChevronDown className={styles.chevronIcon} />*/}
            </AccordionTrigger>
            <div className={styles.benefit_detail_layout}>
              <AccordionContent className={styles.benefit_detail}>
                {benefit.benefitDetail}
              </AccordionContent>
            </div>
          </AccordionItem>
        ))}
      </Accordion>

      {/* 유의사항이 없는 경우 추가할 수 있음 */}
      {!data.benefits.some((b) => b.category === "유의사항") && (
        <div className={styles.noticeItem}>
          <div className={styles.noticeIcon}>
            <FaExclamationTriangle />
          </div>
          <div>
            <div className={styles.benefitMainTitle}>유의사항</div>
            <div className={styles.benefitSubTitle}>
              본 혜택은 카드사 정책에 따라 변경될 수 있습니다.
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
