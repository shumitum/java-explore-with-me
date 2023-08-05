package ru.practicum.mainsrv.utility;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageParam {
    public static PageRequest of(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public static PageRequest of(int from, int size, Sort sort) {
        return PageRequest.of(from > 0 ? from / size : 0, size, sort.descending());
    }
}