import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Banner from "@/components/Banner"; // 올바른 경로로 import
import { Inter } from "next/font/google";
import Header from "@/components/Header"; // 올바른 경로로 import

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});
const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Card Picker: [쉽고 빠르게 찾아드립니다.] ",
  description: "나에게 딱 맞는 카드를 쉽고 빠르게 찾아드립니다.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={inter.className}>
        <Header />
        <main>{children}</main>
      </body>
    </html>
  );
}
