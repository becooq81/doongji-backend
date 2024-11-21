package com.find.doongji.search.util;

import com.find.doongji.search.payload.response.Detail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultParser {
    public static List<Detail> parseToDetails(String input) {
        List<Detail> details = new ArrayList<>();

        String[] entries = input.split("\\)");

        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;

            String[] parts = entry.split("\\(");
            if (parts.length != 2) continue;

            String key = parts[0].trim();
            String[] valuesArray = parts[1].split(",");

            List<String> values = Arrays.stream(valuesArray)
                    .map(String::trim)
                    .filter(value -> !value.isEmpty())
                    .toList();

            details.add(new Detail(key, values));
        }

        return details;
    }

    public static void main(String[] args) {
        String input = "초등학교(괴정초등학교) 대학교(동주대학교, 연세대학교, 과기대학교)";

        // Parse the input into a list of Detail objects
        List<Detail> details = parseToDetails(input);

        for (Detail detail : details) {
            System.out.println(detail);
        }
    }
}
