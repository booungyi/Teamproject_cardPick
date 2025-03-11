import {useState} from "react";
import {router} from "next/client";
import styles from './selectedBenefit.module.css';

interface Benefit {
    id: string;
    name: string;
}


//혜택 선택 - 파라미터로 한번에 백엔드로 보냄
const [benefits, setBenefits] = useState<String[]>([]);

//혜택 선택 함수
const selectBenefit = (benefit: string) => {
    setBenefits(prev => {
        if (prev.includes(benefit)) {
            return prev.filter(b => b !== benefit);
        }
        return [...prev, benefit];
    });
};

export default function selectedBenefit() {


    //선택된 혜택을 URL 파라미터로 추가하여 이동하는 함수

    return (
        <>
            <div className="benefits">
                <header className={styles.box}>카드 혜택 선택</header>
                <section className={styles.option}>
                    {["교통", "주유", "통신", "마트/편의점", "쇼핑", "푸드", "카페/디저트", "뷰티/피트니스"].map((benefit) => (
                        <button
                            key={benefit}
                            onClick={() => selectBenefit(benefit)}
                            className={benefits.includes(benefit) ? styles.selected : styles.unselected}
                        >
                            {benefit}
                        </button>
                    ))}
                    {/*<div>*/}
                    {/*    <button onClick={() => selectBenefit("혜택1")}*/}
                    {/*            className={benefits.includes("혜택1") ? styles.selected : styles.unselected}>*/}
                    {/*        혜택1</button>*/}
                    {/*    <button onClick={() => selectBenefit("혜택2")}*/}
                    {/*            className={benefits.includes("혜택2") ? styles.selected : ""}>*/}
                    {/*        혜택2</button>*/}
                    {/*    <button onClick={() => selectBenefit("혜택3")}*/}
                    {/*            className={benefits.includes("혜택3") ? styles.selected : ""}>*/}
                    {/*        혜택3</button>*/}
                    {/*</div>*/}
                </section>
            </div>
        </>
    )
}