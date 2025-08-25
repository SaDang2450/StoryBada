package com.sadang.storybada.game.dice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`dicebuffer`")
@Entity
public class DiceBuffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long nameId;
    private long hp;
    private boolean result;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
