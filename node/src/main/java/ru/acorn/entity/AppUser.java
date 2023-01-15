package ru.acorn.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.acorn.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId;
    private String username;
    private String firstname;
    private String lastname;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String email;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private UserState userState;
}
