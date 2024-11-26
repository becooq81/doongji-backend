package com.find.doongji.openai.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @NotBlank(message = "Message's role is required")
    private String role;

    @NotBlank(message = "Message's content is required")
    private String content;

}