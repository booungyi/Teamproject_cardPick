"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/router";

export default function CardBenefitPage() {
    const router = useRouter();
    const { cardId } = router.query;
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!cardId) return;

        fetch(`http://localhost:8080/api/card-benefits/${cardId}`)
            .then((res) => res.ok ? res.json() : Promise.reject("Failed to fetch"))
            .then(setData)
            .catch(setError)
            .finally(() => setLoading(false));
    }, [cardId]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading data</p>;
    if (!data) return <p>No data found</p>;

    return (
        <div>
            <h1>{data.cardName}</h1>
            <img
                src={data.cardImageUrl || "/default-card.png"}  // cardImageUrl 적용
                alt={data.cardName}
                style={{ width: "100%", maxWidth: "400px", borderRadius: "8px" }}
            />
            <ul>
                {data.benefits.map((benefit) => (
                    <li key={benefit.id}>
                        <h3>{benefit.benefitName}</h3>
                        <p>{benefit.benefitDetail}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}
