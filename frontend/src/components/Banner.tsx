"use client";

import {useEffect, useState} from "react";
import {fetch} from "undici-types";

interface Ad{
    id: number;
    cardName: string;
    imageUrl: string;
    detailUrl: string;
}

export default function  Banner (){
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
                cardName: "Ad 1: 카드 광고 1 배너",
                imageUrl: "https://via.placeholder.com/300x150?text=Ad+1",
                detailUrl: "https://example.com/ad1"
            },
            {
                id: 2,
                cardName: "Ad 2: 카드 광고 2 배너",
                imageUrl: "https://via.placeholder.com/300x150?text=Ad+2",
                detailUrl: "https://example.com/ad2"
            },
            {
                id: 3,
                cardName: "Ad 3: Free Shipping on All Orders!",
                imageUrl: "https://via.placeholder.com/300x150?text=Ad+3",
                detailUrl: "https://example.com/ad3"
            },
            {
                id: 4,
                cardName: "Ad 4: New Arrivals - Shop Now!",
                imageUrl: "https://via.placeholder.com/300x150?text=Ad+4",
                detailUrl: "https://example.com/ad4"
            },
            {
                id: 5,
                cardName: "Ad 5: 50% Off - Limited Time Only!",
                imageUrl: "https://via.placeholder.com/300x150?text=Ad+5",
                detailUrl: "https://example.com/ad5"
            },
        ];
        setAds(mockAds); // Mock 데이터로 설정
    }, []);



    // 5초마다 자동 슬라이드
    useEffect(() => {
        if (ads.length === 0) return; // 광고가 없으면 실행하지 않음

        const interval = setInterval(() => {
            setCurrentIndex((prevIndex) => (prevIndex + 1) % ads.length);
        }, 5000);

        return () => clearInterval(interval); // 컴포넌트 언마운트 시 정리
    }, [ads]); // ads가 변경될 때마다 실행

    // 인디케이터를 클릭하여 특정 슬라이드로 이동하는 함수
    const goToSlide = (index: number) => {
        setCurrentIndex(index);
    };

    // 이전 슬라이드로 이동
    const goToPrevSlide = () => {
        setCurrentIndex((prevIndex) => (prevIndex - 1 + ads.length) % ads.length);
    };

    // 다음 슬라이드로 이동
    const goToNextSlide = () => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % ads.length);
    };

    return (
        <div className="banner-container">
            <div className="banner">
                <div
                    className="banner-track"
                    style={{ transform: `translateX(-${currentIndex * 100}%)` }} // 슬라이드 효과 적용
                >
                    {ads.map((ad) => (
                        <a key={ad.id} href={ad.detailUrl} className="banner-item">
                            <img src={ad.imageUrl} alt={ad.cardName} />
                            <p>{ad.cardName}</p>
                        </a>
                    ))}
                </div>

                {/* 슬라이드 인디케이터 */}
                <div className="banner-indicators">
                    {ads.map((_, index) => (
                        <span
                            key={index}
                            className={`indicator ${index === currentIndex ? 'active' : ''}`}
                            onClick={() => goToSlide(index)}
                        />
                    ))}
                </div>

                {/* 수동 슬라이드 버튼 */}
                <div className="banner-controls">
                    <button
                        className="banner-control prev"
                        onClick={() => setCurrentIndex((prev) => (prev - 1 + ads.length) % ads.length)}
                    >
                        ◀
                    </button>
                    <button
                        className="banner-control next"
                        onClick={() => setCurrentIndex((prev) => (prev + 1) % ads.length)}
                    >
                        ▶
                    </button>
                </div>
            </div>
        </div>
    );
};
