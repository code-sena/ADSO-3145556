package com.eduaircontrol.msusermanagement.infrastructure.web.dto;

import com.eduaircontrol.msusermanagement.domain.model.PageResult;
import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(List<T> data, PageMeta meta) {

    public static <S, T> PageResponse<T> of(PageResult<S> result, Function<S, T> mapper) {
        List<T> data = result.content().stream().map(mapper).toList();
        return new PageResponse<>(data, new PageMeta(
                result.page(), result.limit(), result.total(), result.totalPages()));
    }

    public record PageMeta(int page, int limit, long total, int totalPages) {
    }
}
