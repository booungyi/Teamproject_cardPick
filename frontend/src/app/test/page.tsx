import PopularCards from "@/components/ui/popular-cards";

export default function Test() {
    const popularCards = [
        { rank: 1, name: "신한카드 Mr.Life" },
        { rank: 2, name: "삼성카드 taptap O" },
        { rank: 3, name: "삼성카드 & MILEAGE PLATINUM" },
        { rank: 4, name: "현대카드 M" },
        { rank: 5, name: "카드의정석 EVERY MILE SKYPASS" },
        { rank: 6, name: "LOCA LIKIT 1.2" },
        { rank: 7, name: "KB국민 My WE:SH 카드" },
        { rank: 8, name: "신한카드 처음(ANNIVERSE)" },
        { rank: 9, name: "LOCA 365 카드" },
        { rank: 10, name: "신한카드 Deep Oil" },
    ];

    return (
        <main className="p-4">
            <PopularCards cards={popularCards} />
        </main>
    );
}
