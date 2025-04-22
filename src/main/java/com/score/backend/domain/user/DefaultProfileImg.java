package com.score.backend.domain.user;

import lombok.Getter;

@Getter
public enum DefaultProfileImg {
    RABBIT(1, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/142237c5-6ac6-4a8a-9f9e-cf43a4e0f163"),
    PANDA(2, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/e629ec74-adb8-4b7c-a8d4-9f40d8a53cd3"),
    CAT(3, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/21809541-cb0a-49d7-a71b-493f87c133c4"),
    TURTLE(4, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/e8889d40-a042-427e-8b3b-78e98e5bf608"),
    DOG(5, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/09c6b6a6-e7b7-49f1-8822-349d48b52089");

    private final int id;
    private final String url;

    DefaultProfileImg(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public static String getUrlById(int id) {
        for (DefaultProfileImg defaultProfileImg : DefaultProfileImg.values()) {
            if (defaultProfileImg.getId() == id) {
                return defaultProfileImg.getUrl();
            }
        }
        return RABBIT.getUrl();
    }
}
