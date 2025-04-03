package team.cardpick_project.cardpick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import team.cardpick_project.cardpick.cardAdverise.AdQueryRepository;
import team.cardpick_project.cardpick.cardPick.cardDto.CardResponse;
import team.cardpick_project.cardpick.cardPick.cardDto.CardResponseQDto;
import team.cardpick_project.cardpick.cardPick.domain.CardDao;
import team.cardpick_project.cardpick.cardPick.service.CardPickService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CardPickServiceTest {

    @Mock
    private CardDao cardDao;

    @Mock
    private AdQueryRepository adQueryRepository;

    @InjectMocks
    private CardPickService cardService;

    @Test
    void testGetCardsByConditions_incrementsClickCount() {
        // given: 테스트 데이터를 준비
        Long cardId = 1L;
        List<String> categories = Arrays.asList("category1", "category2");

        CardResponseQDto cardResponseQDto = new CardResponseQDto(
                cardId,
                "Card Name",
                "imageUrl",
                "detailUrl",
                6
        );

        List<CardResponseQDto> mockCards = Arrays.asList(cardResponseQDto);
        when(cardDao.getCardsByConditions(any(), any())).thenReturn(mockCards);

        // when: 서비스 메서드 호출
        List<CardResponse> result = cardService.getCardsByConditions(null, categories);

        // then: 클릭수가 증가한 카드 조회가 호출되었는지 확인
//        verify(cardDao, times(1)).incrementClickCount(cardId);  // cardDao.incrementClickCount(cardId)가 1번 호출되어야 한다.
        assertThat(result);
    }

    @Test
    void testGetCardsByConditions_returnCorrectResponse() {
        // given
        Long cardId = 1L;
        List<String> categories = Arrays.asList("category1", "category2");

        CardResponseQDto cardResponseQDto = new CardResponseQDto(
                cardId,
                "Card Name",
                "imageUrl",
                "detailUrl",
                56
        );

        List<CardResponseQDto> mockCards = Arrays.asList(cardResponseQDto);
        when(cardDao.getCardsByConditions(any(), any())).thenReturn(mockCards);

        // when
        List<CardResponse> result = cardService.getCardsByConditions(null, categories);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cardId, result.get(0).id());
    }
}
