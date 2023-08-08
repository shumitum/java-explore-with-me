package ru.practicum.mainsrv.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.compilation.dto.NewCompilationDto;
import ru.practicum.mainsrv.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainsrv.compilation.dto.ViewCompilationDto;
import ru.practicum.mainsrv.event.EventRepository;
import ru.practicum.mainsrv.utility.PageParam;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ViewCompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Long> eventIds = newCompilationDto.getEvents();
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        updateCompilationEvents(eventIds, compilation);
        return compilationMapper.toViewCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public ViewCompilationDto getCompilationById(Long compId) {
        return compilationMapper.toViewCompilationDto(findCompilationById(compId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return compilationRepository.getCompilationsByPinned(pinned, PageParam.of(from, size)).stream()
                .map(compilationMapper::toViewCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        checkCompilationExistence(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public ViewCompilationDto updateCompilation(UpdateCompilationRequest updCompRequest, Long compId) {
        Compilation compilation = findCompilationById(compId);
        Optional.ofNullable(updCompRequest.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updCompRequest.getTitle()).ifPresent(compilation::setTitle);
        List<Long> eventIds = updCompRequest.getEvents();
        updateCompilationEvents(eventIds, compilation);
        return compilationMapper.toViewCompilationDto(compilation);
    }

    @Transactional
    public void updateCompilationEvents(List<Long> eventIds, Compilation compilation) {
        if (eventIds != null && !eventIds.isEmpty()) {
            compilation.setEvents(eventRepository.getEventsByIdIn(eventIds));
        } else {
            compilation.setEvents(Collections.emptyList());
        }
    }

    @Transactional(readOnly = true)
    public void checkCompilationExistence(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NoSuchElementException(String.format("Подборки событий с ID=%d не существует", compId));
        }
    }

    @Transactional(readOnly = true)
    public Compilation findCompilationById(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Подборки событий с ID=%d не существует", compId)));
    }
}
