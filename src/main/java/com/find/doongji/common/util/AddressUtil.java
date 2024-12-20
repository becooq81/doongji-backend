package com.find.doongji.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

// aka 도로명주소, 신주소
public class AddressUtil {

    public static OldAddressComponents parseOldAddress(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        String regex = "(.*?\\d[-\\d]*)(.*)";

        // Match the input string
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String jibunAddress = matcher.group(1).trim(); // Group 1: Everything up to and including the last number
            String aptName = matcher.group(2).trim();      // Group 2: Everything after the last number
            return new OldAddressComponents(jibunAddress, aptName);
        }

        // If no match is found, consider the entire input as jibunAddress
        return new OldAddressComponents(input.trim(), "");
    }

    public static AddressComponents parseAddress(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        String[] words = cleanAddress(input).split("\\s+");
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

    public static String helpSeongnam(String address) {
        return address.replaceAll("성남(?!\\s)", "성남 ");
    }

    public static String cleanAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "";
        }

        String cleanedAddress = address.replaceAll("\\s*\\([^)]*\\)", "").trim();

        if (cleanedAddress.contains("서울특별")) {
            cleanedAddress = cleanedAddress.replace("서울특별", "서울");
        }
        cleanedAddress = cleanedAddress.replace("광역", ""); // Retain other replacements


        return cleanedAddress;
    }

    public static void main(String[] args) {
        String input = "서울시 종로구 창신길 140-10 (창신동, 엠아이디그린아파트)";
        AddressComponents components = parseAddress(input);
        System.out.println(components);

        String input2 = "인천 서구 탁옥로51번길 13-6 (카카오호텔)";
        AddressComponents components2 = parseAddress(input2);
        System.out.println(components2);

        System.out.println(cleanAddress(input));

        String input3 = "경기도 과천시 부림동 41 주공아파트";
        OldAddressComponents oldAddressComponents = parseOldAddress(input3);
        System.out.println(oldAddressComponents);
    }

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

    @Getter
    @AllArgsConstructor
    public static class OldAddressComponents {
        private final String jibunAddress;
        private final String aptName;

        @Override
        public String toString() {
            return "OldAddressComponents{" +
                    "jibunAddress='" + jibunAddress + '\'' +
                    ", aptName='" + aptName + '\'' +
                    '}';
        }
    }


}