package ru.acorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.acorn.entity.AppDocument;

@Repository
public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}
