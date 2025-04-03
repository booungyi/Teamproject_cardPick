"use client";

import { useState, useEffect } from "react";
import { Card } from "@/app/page";
import useEmblaCarousel from "embla-carousel-react";

const PopularCards = ({ cards }: { cards: Card[] }) => {
    const [isExpanded, setIsExpanded] = useState(false);
    const [emblaRef, emblaApi] = useEmblaCarousel({ loop: true, align: "start" });

    // 자동 슬라이드 기능
    useEffect(() => {
        if (!emblaApi) return;
        const interval = setInterval(() => emblaApi.scrollNext(), 2000); // 2초마다 이동
        return () => clearInterval(interval);
    }, [emblaApi]);

    return (
        <div className="relative inline-block">
            {/* 드롭다운 목록 */}
            <div className="mt-2 w-80 bg-white shadow-lg rounded-lg p-4">
                {/* 제목을 클릭하면 전체 목록 표시 */}
                <h3
                    className="font-semibold mb-2 text-center cursor-pointer hover:text-blue-500"
                    onClick={() => setIsExpanded(!isExpanded)}
                >
                    신용카드 실시간 인기순위
                </h3>

                {isExpanded ? (
                    // 전체 차트 목록
                    <ul>
                        {cards.map((card, index) => (
                            <li key={index} className="py-2 border-b last:border-none">
                                <span className="font-bold">{index + 1}.</span> {card.cardName}
                            </li>
                        ))}
                    </ul>
                ) : (
                    // 자동 슬라이드 목록
                    <div className="overflow-hidden" ref={emblaRef}>
                        <div className="flex">
                            {cards.map((card, index) => (
                                <div
                                    key={index}
                                    className="flex-none w-full p-4 bg-gray-100 text-center rounded-lg shadow-md"
                                >
                                    <span className="font-bold">{index + 1}.</span> {card.cardName}
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default PopularCards;
