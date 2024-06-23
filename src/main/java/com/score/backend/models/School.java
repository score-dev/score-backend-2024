package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class School extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "school_id")
    private Long id;

    private String schoolName;

    private String schoolAddress;

    @OneToMany(mappedBy="school")
    @JsonIgnore
    private List<User> students = new ArrayList<>();

    @OneToMany(mappedBy = "belongingSchool")
    @JsonIgnore
    private List<Group> groups = new ArrayList<>();


}
