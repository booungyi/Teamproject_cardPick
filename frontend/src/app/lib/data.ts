export type Category = '쇼핑' | '교통' | '통신' | '할인_및_적립' | '주유' | '항공' | '음식';

export interface CardInfo {
    cardName: string;
    imageUrl: string;
    detailUrl: string;
    isAdCard: boolean;
}

export async function getTotalCardCount(){
    const response = await fetch("http://localhost:8080/api/card_picks/conditions/count");
    const count = await response.json();

    return count;
}

export async function getFilteredCards(categories:Category[]):Promise<CardInfo[]> {
    const queryString = `categories=${categories.join(",")}`;
    const response = await fetch(`http://localhost:8080/api/card_picks/conditions?${queryString}`);
    const cards = await response.json();

    return cards;


}