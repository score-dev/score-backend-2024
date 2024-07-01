package com.score.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.score.backend.config.BaseEntity;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "group")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupId")
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School belongingSchool;

    @Column(name = "groupName", nullable = false, unique = true, length = 15)
    private String groupName;

    @Column(name = "groupDescription", nullable = false, length = 200)
    private String groupDescription;

    @Column(name = "groupImg", nullable = false)
    private String groupImg;

    @Column(name = "isPrivate", nullable = false)
    private boolean isPrivate;

    @Column(name = "groupPassword", length = 4)
    private String groupPassword;

    @Column(name = "userLimit", length = 50)
    private int userLimit;

    @CreatedDate
    private LocalDateTime groupCreatedAt;

    @LastModifiedDate
    private LocalDateTime groupUpdatedAt;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=false)
    private User admin;

    @OneToMany(mappedBy = "group")
    private Set<User> members; //회원들과의 관계

}
