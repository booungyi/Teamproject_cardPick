"use client";

import {useEffect, useState} from "react";
import useEmblaCarousel from "embla-carousel-react";
import styles from "./Banner.module.css";
import {fetch} from "undici-types";

interface Ad{
    id: number;
    cardName: string;
    image?: string;
    detailUrl: string;
}

export default function  Banner (){
    const [emblaRef, emblaApi] = useEmblaCarousel({ loop: true });
    const [ads, setAds] = useState<Ad[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0);

    //페이지 로드 시 광고 데이터 가져오기 csr방식
    //페이지 로드 시 최초 1번만 실행
    //백엔드에서 실제로 Ad[] 형태의 JSON을 반환하는지 확실해야함
    // useEffect(() => {
    //     const fetchAds = async () => {
    //         try {
    //             const res = await fetch("/ad");
    //             const data = await res.json() as Ad[]; // 👈 해결 방법 적용
    //             setAds(data);
    //         } catch (error) {
    //             console.error("광고 로드 실패", error);
    //         }
    //     };
    //
    //     fetchAds();
    // }, []);

    // 페이지 로드 시 광고 데이터 가져오기 (현재는 Mock 데이터로 대체)
    useEffect(() => {
        // 임시로 Mock 데이터를 사용
        const mockAds: Ad[] = [
            {
                id: 1,
                //카드
                cardName: "Ad 1: 카드 광고 이미지",
                image: "/images/banner3.jpg",
                detailUrl: "https://card.kbcard.com/CRD/DVIEW/HCAMCXPRICAC0076?mainCC=a&cooperationcode=09061&solicitorcode=7030023014&utm_source=google_sa&utm_medium=cpc&utm_campaign=goodday_card&utm_content=goodday_card&utm_term=KB%EA%B5%BF%EB%8D%B0%EC%9D%B4%EC%B9%B4%EB%93%9C&gad_source=1&gclid=EAIaIQobChMIif3Wxqa9jAMVkmoPAh1BJyOKEAAYASAAEgKMcvD_BwE"
            },
            {
                id: 2,
                cardName: "Ad 2: 카드 광고 2 배너",
                image: "/images/banner1.jpg",
                detailUrl: "https://example.com/ad2"
            },
            {
                id: 3,
                cardName: "Ad 3: Free Shipping on All Orders!",
                image: "/images/banner3.jpg",
                detailUrl: "https://example.com/ad3"
            },
            {
                id: 4,
                cardName: "Ad 4: New Arrivals - Shop Now!",
                image: "/images/banner1.jpg",
                detailUrl: "https://example.com/ad4"
            },
            {
                id: 5,
                cardName: "Ad 5: 50% Off - Limited Time Only!",
                image: "/images/banner3.jpg",
                detailUrl: "https://example.com/ad5"
            },
        ];
        setAds(mockAds); // Mock 데이터로 설정
    }, []);

    // 자동 슬라이드 기능
    useEffect(() => {
        if (!emblaApi) return;
        const autoplay = setInterval(() => {
            emblaApi.scrollNext();
        }, 5000);
        return () => clearInterval(autoplay);
    }, [emblaApi]);

    // 현재 슬라이드 감지
    useEffect(() => {
        if (!emblaApi) return;
        const updateIndex = () => {
            setCurrentIndex(emblaApi.selectedScrollSnap());
        };
        emblaApi.on("select", updateIndex);
        return () => emblaApi.off("select", updateIndex);
    }, [emblaApi]);

    return (
        <div className={styles.bannerContainer}>
            <div className={styles.bannerWrapper} ref={emblaRef}>
                <div className={styles.bannerTrack}>
                    {ads.map((ad) => (
                        <a key={ad.id} href={ad.detailUrl} target="_blank" rel="noopener noreferrer" className={styles.bannerItem}>
                            <img src={ad.image} alt={ad.cardName} className={styles.bannerImage} />
                            {/*<p className={styles.bannerText}>{ad.cardName}</p>*/}
                        </a>
                    ))}
                </div>
            </div>

            {/* 네비게이션 버튼 */}
            <button className={`${styles.navButton} ${styles.left}`} onClick={() => emblaApi?.scrollPrev()}>
                ◀
            </button>
            <button className={`${styles.navButton} ${styles.right}`} onClick={() => emblaApi?.scrollNext()}>
                ▶
            </button>

            {/* 인디케이터 */}
            <div className={styles.indicatorContainer}>
                {ads.map((_, index) => (
                    <button
                        key={index}
                        className={`${styles.indicator} ${currentIndex === index ? styles.active : ""}`}
                        onClick={() => emblaApi?.scrollTo(index)}
                        aria-label={`Go to slide ${index + 1}`}
                    />
                ))}
            </div>
        </div>
    );
};
