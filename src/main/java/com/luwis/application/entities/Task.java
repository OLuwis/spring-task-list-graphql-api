package com.luwis.application.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "Tasks", indexes = @Index(name = "uid_index", columnList = "user_id"))
@NoArgsConstructor
@RequiredArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(length = 50)
    private String title;

    @NonNull
    @Column(length = 100)
    private String description;

    @Column(insertable = false)
    private Boolean pending = false;

    @Column(insertable = false)
    private LocalDate createdAt = LocalDate.now();

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}