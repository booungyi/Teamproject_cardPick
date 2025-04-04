import Header from "@/components/Header";
import PopularCards from "@/components/ui/popular-cards";
import { Card } from "@/app/page";

export default async function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  const res = await fetch("http://localhost:8080/api/card_picks/popular", {
    cache: "no-store",
  });
  const cards: Card[] = await res.json();

  return (
    <html lang="ko">
      <body>
        <Header cards={cards} />
        <main>{children}</main>
      </body>
    </html>
  );
}
