package ru.acorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acorn.entity.RawData;

public interface RawDataRepository extends JpaRepository <RawData, Long> {
}
