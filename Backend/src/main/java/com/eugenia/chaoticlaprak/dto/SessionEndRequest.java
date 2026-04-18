package com.eugenia.chaoticlaprak.dto;

import lombok.Data;

@Data
public class SessionEndRequest {
    private Long sessionId;
    private Long durationSeconds;
}