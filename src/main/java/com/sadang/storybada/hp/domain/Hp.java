package com.sadang.storybada.hp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder =  true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name="`hp`")
@Entity
public class Hp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long nameId;
    private long point;
    private String game;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
