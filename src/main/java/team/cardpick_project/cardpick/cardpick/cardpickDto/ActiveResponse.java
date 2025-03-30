package team.cardpick_project.cardpick.cardpick.cardpickDto;

import java.time.LocalDateTime;

public record ActiveResponse(Long id,
                             String cardName,
                             String imageUrl,
                             String detailUrl
                             ){
}
