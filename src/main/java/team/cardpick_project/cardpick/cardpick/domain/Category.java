package team.cardpick_project.cardpick.cardpick.domain;

public enum Category {
    쇼핑, 교통, 통신, 할인_및_적립, 주유, 항공, 음식, 기타;

    public static Category fromString(String value){
        if ("할인 및 적립".equals(value)){
            return 할인_및_적립;
        }
        for (Category category : Category.values()){
            if (category.name().equals(value)){
                return category;
            }
        }
        return 기타;
    }
}