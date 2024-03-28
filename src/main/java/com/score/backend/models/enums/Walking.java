package com.score.backend.models.enums;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Walking extends Exercise {
    private int distance;
}
