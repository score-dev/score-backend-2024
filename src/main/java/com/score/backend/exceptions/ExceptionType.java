package com.score.backend.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionType {
    DUPLICATE_NICKNAME("이미 사용 중인 닉네임입니다."),
    GROUP_ADMIN_WITHDRAWAL("그룹의 방장은 서비스를 탈퇴할 수 없습니다. 고객센터로 문의해주세요."),
    USERS_SCHOOL_GROUP_UNMATCHED("가입하려는 그룹의 소속 학교가 유저의 학교 정보와 일치하지 않습니다."),
    FULL_GROUP("정원이 가득 차 가입할 수 없는 그룹입니다."),
    ALREADY_JOINED_GROUP("이미 가입되어 있는 그룹입니다."),
    ALREADY_FRIEND("이미 친구로 등록되어 있습니다."),
    ALREADY_BLOCKED("이미 차단한 유저입니다."),
    TOO_FREQUENT_SCHOOL_CHANGING("마지막으로 학교 정보를 변경한 후 30일이 경과하지 않아 학교 정보를 수정할 수 없습니다."),
    UNSUPPORTED_FILE_TYPE("지원하지 않는 파일입니다. 확장자가 jpg, png인 파일만 업로드 가능합니다."),
    EXCEEDED_FILE_SIZE("파일의 크기가 너무 큽니다. 3MB 이하의 파일만 업로드 가능합니다."),
    NICKNAME_FORMAT_ERROR("형식에 맞지 않는 닉네임입니다. 10자 이내의 한글만 입력 가능합니다."),
    GROUP_NAME_FORMAT_ERROR("형식에 맞지 않는 그룹명입니다. 20자 이내의 한글, 숫자, 영어만 입력 가능합니다."),
    GROUP_DESC_FORMAT_ERROR("형식에 맞지 않는 그룹 소개입니다. 200자 이내의 한글, 숫자, 영어, 특수 문자만 입력 가능합니다."),
    GROUP_PW_FORMAT_ERROR("형식에 맞지 않는 비밀번호입니다. 4자리의 숫자만 입력 가능합니다."),
    GROUP_MEMBER_LIMIT_FORMAT_ERROR("그룹의 최대 정원은 50명을 초과할 수 없습니다.");

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }
}
