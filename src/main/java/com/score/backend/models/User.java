package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    private int grade;

    private int height;

    private int weight;

    private String profileImg;

    private Time goal;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<User> mates = new ArrayList<>();

    private boolean marketing;

    private boolean push;

    private String refreshToken;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
