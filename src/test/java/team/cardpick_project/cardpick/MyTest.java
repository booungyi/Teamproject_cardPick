package team.cardpick_project.cardpick;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDateTime;

public class MyTest {

    @Test
    void name() {
        int sum = 0;
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        for (int i = 0; i < 300000; i++) {
            sum += i;
        }
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        double secDiffTime = (afterTime - beforeTime)/1000.0; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);
        System.out.println("sum = " + sum);
    }
}
