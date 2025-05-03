CREATE TABLE feed_report
(
    feed_report_id BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                NULL,
    exercise_id    BIGINT                NULL,
    reason         VARCHAR(255)          NULL,
    comment        VARCHAR(255)          NULL,
    CONSTRAINT pk_feedreport PRIMARY KEY (feed_report_id)
);

ALTER TABLE feed_report
    ADD CONSTRAINT FK_FEEDREPORT_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercise (exercise_id);

ALTER TABLE feed_report
    ADD CONSTRAINT FK_FEEDREPORT_ON_USER FOREIGN KEY (user_id) REFERENCES user (user_id);