package team.cardpick_project.cardpick.cardPick.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Mbti {
    J(new ArrayList<>(List.of(Category.쇼핑, Category.할인_및_적립))),
    P(new ArrayList<>(List.of(Category.음식))),
    T(new ArrayList<>(List.of(Category.통신, Category.항공))),
    F(new ArrayList<>(List.of(Category.쇼핑, Category.음식))),
    S(new ArrayList<>(List.of(Category.교통, Category.주유))),
    N(new ArrayList<>(List.of(Category.항공)));

    private final List<Category> categories;

    public static Mbti fromString(String value) {
        for (Mbti mbti : Mbti.values()) {
            if (mbti.name().equals(value)) {
                return mbti;
            }
        }
        return N;
    }
}
