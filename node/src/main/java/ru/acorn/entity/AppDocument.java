package ru.acorn.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "app_document")
@Setter @Getter
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String telegramDocId;
    private String filename;
    private String mimeType;
    private Integer fileSize;
    @OneToOne
    private BinaryContent binaryContent;


}
