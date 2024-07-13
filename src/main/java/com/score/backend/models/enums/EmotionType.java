package com.score.backend.models.enums;

public enum EmotionType {
    LIKE, BEST, SUPPORT, CONGRAT, FIRST; // 좋아요 최고예요 응원해요 축하해요 1등이에요

    public static EmotionType create(String requestType) {
        for (EmotionType emotionType : EmotionType.values()) {
            if (emotionType.name().equals(requestType)) {
                return emotionType;
            }
        }
        throw new IllegalArgumentException("Invalid emotion type: " + requestType);
    }
}
