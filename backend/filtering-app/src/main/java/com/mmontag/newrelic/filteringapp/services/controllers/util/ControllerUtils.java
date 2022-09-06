package com.mmontag.newrelic.filteringapp.services.controllers.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ControllerUtils {

    default <T, R> List<R> mapAllDeduplicated(JpaRepository<T, ?> repository, Function<T, R> mapper) {
        return repository.findAll().stream().map(mapper).distinct().collect(Collectors.toList());
    }

}
