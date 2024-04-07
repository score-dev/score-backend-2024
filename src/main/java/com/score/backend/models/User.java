package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.BaseEntity;
import com.score.backend.models.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    private int grade;

    private int height;

    private int weight;

    private String profileImg;

    private Time goal;

    @OneToMany(mappedBy="agent")
    @JsonIgnore
    private List<Exercise> feeds = new ArrayList<>();

    private boolean marketing;

    private boolean push;

    private String refreshToken;

    private String loginKey;
}
