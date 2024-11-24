package com.find.doongji.openai.payload.response;

import com.find.doongji.openai.payload.request.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Choice {

    private int index;
    private Message message;

}
