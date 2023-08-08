package ru.practicum.mainsrv.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> getCompilationsByPinned(Boolean pinned, PageRequest page);
}