import { useEffect, useState } from "react";
import "./popular-card.module.css";

interface AdData {
    id: number;
    imageUrl: string;
    text: string;
}

export default function AdList() {
    const [ads, setAds] = useState<AdData[]>([]);

    useEffect(() => {
        fetch("https://your-backend-api.com/ads")
            .then((res) => res.json())
            .then((data) => {
                console.log("Fetched ads:", data); // 디버깅용
                setAds(data);
            })
            .catch((err) => console.error("Failed to fetch ads:", err));
    }, []);

    return (
        <div className="ad-list-container">
            {ads.map((ad) => (
                <div key={ad.id} className="ad-item">
                    <img src={ad.imageUrl} alt={ad.text} className="ad-image" />
                    <span className="ad-text">{ad.text}</span>
                </div>
            ))}
        </div>
    );
}