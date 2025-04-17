package com.example.myapp.websocket.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    // MySQL에서 사용되는 "yyyy-MM-dd HH:mm:ss" 형식으로 변환하는 메서드
    public static String convertToMySQLDateFormat(Instant instant) {
        // UTC 시간대에 맞춰 Instant를 변환하고, 지정된 패턴으로 포맷팅
        return instant.atZone(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static void main(String[] args) {
        // 예시로 현재 시간을 Instant로 가져옵니다.
        Instant now = Instant.now();

        // MySQL에 맞는 형식으로 변환
        String formattedDate = convertToMySQLDateFormat(now);

        // 결과 출력
        System.out.println("MySQL Date Format: " + formattedDate);
    }
}