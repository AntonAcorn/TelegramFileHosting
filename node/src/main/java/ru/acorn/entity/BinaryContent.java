package ru.acorn.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "binary_content")
@Builder
@Getter @Setter
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    byte [] fileArraysOfBytes;
}
