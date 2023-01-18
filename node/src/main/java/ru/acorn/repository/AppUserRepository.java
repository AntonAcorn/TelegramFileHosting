package ru.acorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.acorn.entity.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional <AppUser> findAppUserByTelegramUserId(Long telegramUserId);
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findAppUserByEmail(String email);
}
