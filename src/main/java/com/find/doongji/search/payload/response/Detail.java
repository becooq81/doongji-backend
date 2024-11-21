package com.find.doongji.search.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Detail {
    private String key;
    private List<String> values;

    @Override
    public String toString() {
        return "Detail{" +
                "key='" + key + '\'' +
                ", values=" + values + '\'' + "first value: " + values.get(0) + '\'' +
                '}';
    }
}
