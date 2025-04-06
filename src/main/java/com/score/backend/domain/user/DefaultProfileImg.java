package com.score.backend.domain.user;

import lombok.Getter;

@Getter
public enum DefaultProfileImg {
    RABBIT(1, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/d874fe25-d48b-41aa-bf12-30e5115711ac"),
    PANDA(2, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/6c3633d3-773a-443f-b390-f5a0ae8e9229"),
    CAT(3, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/fc2aded8-2c00-4594-9a2a-9c8553e42df3"),
    TURTLE(4, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/087f9dd4-75ad-404a-84d6-dc8cbc89ec06"),
    DOG(5, "https://score-server-bucket.s3.ap-northeast-2.amazonaws.com/57592550-d7c7-4802-8792-5f9971067e06");

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
