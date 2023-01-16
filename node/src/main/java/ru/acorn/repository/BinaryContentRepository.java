package ru.acorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.acorn.entity.BinaryContent;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
