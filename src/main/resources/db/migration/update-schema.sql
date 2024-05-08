CREATE TABLE score.exercise
(
    exercise_id      BIGINT NOT NULL,
    dtype            VARCHAR(31) NULL,
    created_at       datetime NULL,
    updated_at       datetime NULL,
    created_by       VARCHAR(255) NULL,
    last_modified_by VARCHAR(255) NULL,
    user_id          BIGINT NULL,
    started_at       datetime NULL,
    completed_at     datetime NULL,
    reduced_kcal     INT    NOT NULL,
    location         VARCHAR(255) NULL,
    weather          VARCHAR(255) NULL,
    temperature      INT    NOT NULL,
    emotion          VARCHAR(255) NULL,
    exercise_pic     VARCHAR(255) NULL,
    content          VARCHAR(255) NULL,
    distance         INT    NOT NULL,
    CONSTRAINT pk_exercise PRIMARY KEY (exercise_id)
);

CREATE TABLE score.exercise_user
(
    user_id     BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    CONSTRAINT pk_exerciseuser PRIMARY KEY (user_id, exercise_id)
);

CREATE TABLE score.user
(
    user_id                 BIGINT       NOT NULL,
    created_at              datetime NULL,
    updated_at              datetime NULL,
    created_by              VARCHAR(255) NULL,
    last_modified_by        VARCHAR(255) NULL,
    nickname                VARCHAR(255) NOT NULL,
    grade                   INT          NOT NULL,
    height                  INT          NOT NULL,
    weight                  INT          NOT NULL,
    profile_img             VARCHAR(255) NULL,
    goal                    time NULL,
    level                   INT          NOT NULL,
    point                   INT          NOT NULL,
    last_exercise_date_time datetime NULL,
    consecutive_date        INT          NOT NULL,
    cumulative_time DOUBLE NOT NULL,
    cumulative_distance DOUBLE NOT NULL,
    marketing               BIT(1)       NOT NULL,
    push                    BIT(1)       NOT NULL,
    refresh_token           VARCHAR(255) NULL,
    login_key               VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

ALTER TABLE score.user
    ADD CONSTRAINT uc_user_nickname UNIQUE (nickname);

ALTER TABLE score.exercise_user
    ADD CONSTRAINT FK_EXERCISEUSER_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES score.exercise (exercise_id);

ALTER TABLE score.exercise_user
    ADD CONSTRAINT FK_EXERCISEUSER_ON_USER FOREIGN KEY (user_id) REFERENCES score.user (user_id);

ALTER TABLE score.exercise
    ADD CONSTRAINT FK_EXERCISE_ON_USER FOREIGN KEY (user_id) REFERENCES score.user (user_id);