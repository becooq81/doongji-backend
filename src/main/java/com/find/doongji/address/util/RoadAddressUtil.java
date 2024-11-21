package com.find.doongji.address.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

// aka 도로명주소, 신주소
public class RoadAddressParser {

    @Getter
    @AllArgsConstructor
    public static class AddressComponents {
        private final String roadNm;
        private final String roadNmBonbun;
        private final String roadNmBubun;

        @Override
        public String toString() {
            return "AddressComponents{" +
                    "roadNm='" + roadNm + '\'' +
                    ", roadNmBonbun='" + roadNmBonbun + '\'' +
                    ", roadNmBubun=" + roadNmBubun +
                    '}';
        }
    }

    public static AddressComponents parseAddress(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        String[] words = input.trim().split("\\s+");
        List<String> filteredWords = Arrays.stream(words)
                .filter(word -> !(word.startsWith("(") && word.endsWith(")")))
                .toList();

        if (filteredWords.size() < 2) {
            throw new IllegalArgumentException("Input string must contain at least two valid words");
        }

        String roadNm = filteredWords.get(filteredWords.size() - 2);
        String lastWord = filteredWords.get(filteredWords.size() - 1);

        String roadNmBonbun;
        String roadNmBubun = "0";

        if (lastWord.contains("-")) {
            String[] parts = lastWord.split("-", 2);
            roadNmBonbun = parts[0];
            roadNmBubun = parts[1];
        } else {
            roadNmBonbun = lastWord;
        }

        return new AddressComponents(roadNm, roadNmBonbun, roadNmBubun);
    }

    public static String cleanAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "";
        }

        String cleanedAddress = address.replaceAll("\\s*\\([^)]*\\)\\s*$", "").trim();
        return cleanedAddress;
    }

    public static void main(String[] args) {
        String input = "서울특별시 강남구 테헤란로 212 (역삼동)";
        AddressComponents components = parseAddress(input);
        System.out.println(components);

        String input2 = "인천 서구 탁옥로51번길 13-6 (카카오호텔)";
        AddressComponents components2 = parseAddress(input2);
        System.out.println(components2);

        System.out.println(cleanAddress(input));
    }


}
