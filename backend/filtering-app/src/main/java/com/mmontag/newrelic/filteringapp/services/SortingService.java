package com.mmontag.newrelic.filteringapp.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import javax.lang.model.SourceVersion;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mmontag.newrelic.filteringapp.exception.SortingException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SortingService<T> {

    public enum SortDirection {
        ASCENDING,
        DESCENDING
    }

    public enum NullOrderingBehavior {
        NULLS_FIRST,
        NULLS_LAST,
        CUSTOM
    }
    
    private SortDirection DEFAULT_SORT_DIRECTION = SortDirection.ASCENDING;

    private NullOrderingBehavior DEFAULT_NULL_BEHAVIOR = NullOrderingBehavior.NULLS_LAST;

    private final HashMap<String, Comparator<T>> comparatorsByField = new HashMap<>();

    private final HashMap<String, SortDirection> defaultSortDirectionsByField = new HashMap<>();

    private final HashMap<String, NullOrderingBehavior> defaultNullBehaviorsByField = new HashMap<>();

    public boolean isSortableBy(String fieldName) {
        return comparatorsByField.containsKey(fieldName);
    }

    public SortingService<T> registerField(String fieldName, Comparator<T> comparator, SortDirection defaultSortDirection, NullOrderingBehavior defaultNullBehavior) {
        if (isValidFieldName(fieldName)) {
            comparatorsByField.put(fieldName, comparator);
            
            if (defaultSortDirection != null && defaultSortDirection != DEFAULT_SORT_DIRECTION) {
                defaultSortDirectionsByField.put(fieldName, defaultSortDirection);
            }

            if (defaultNullBehavior != null && defaultNullBehavior != DEFAULT_NULL_BEHAVIOR) {
                defaultNullBehaviorsByField.put(fieldName, defaultNullBehavior);
            }

        } else {
            throw new IllegalArgumentException("Illegal field name: " + fieldName + " - Reason: Sortable field names must be valid Java identifiers.");
        }

        return this;
    }

    public SortingService<T> registerField(String fieldName, Comparator<T> comparator, SortDirection defaultSortDirection) {
        return registerField(fieldName, comparator, defaultSortDirection, DEFAULT_NULL_BEHAVIOR);
    }

    public SortingService<T> registerField(String fieldName, Comparator<T> comparator, NullOrderingBehavior defaultNullBehavior) {
        return registerField(fieldName, comparator, DEFAULT_SORT_DIRECTION, defaultNullBehavior);
    }

    public SortingService<T> registerField(String fieldName, Comparator<T> comparator) {
        return registerField(fieldName, comparator, DEFAULT_SORT_DIRECTION, DEFAULT_NULL_BEHAVIOR);
    }

    public <R extends Comparable<? super R>> SortingService<T> registerField(String fieldName, Function<? super T, ? extends R> fieldExtractor, SortDirection defaultSortDirection, NullOrderingBehavior defaultNullBehavior) {
        return registerField(fieldName, fieldExtractor == null ? null : Comparator.comparing(fieldExtractor), defaultSortDirection, defaultNullBehavior);
    }

    public <R extends Comparable<? super R>> SortingService<T> registerField(String fieldName, Function<? super T, ? extends R> fieldExtractor, SortDirection defaultSortDirection) {
        return registerField(fieldName, fieldExtractor, defaultSortDirection, DEFAULT_NULL_BEHAVIOR);
    }

    public <R extends Comparable<? super R>> SortingService<T> registerField(String fieldName, Function<? super T, ? extends R> fieldExtractor, NullOrderingBehavior defaultNullBehavior) {
        return registerField(fieldName, fieldExtractor, DEFAULT_SORT_DIRECTION, defaultNullBehavior);
    }

    public <R extends Comparable<? super R>> SortingService<T> registerField(String fieldName, Function<? super T, ? extends R> fieldExtractor) {
        return registerField(fieldName, fieldExtractor == null ? null : Comparator.comparing(fieldExtractor));
    }

    /**
     * Sorts a given list of elements according to a sort field, sort direction, and null-ordering behavior.  The sort field must be 
     * registered ahead of time via {@code SortingService#registerField(...)}.
     * <pre/>
     * Passing in {@code null} for the sortDirection and/or nullBehavior args will use default values (field-level, if registered,
     * otherwise instance-level).
     * <pre/>
     * NOTE: THIS IS AN IN-PLACE SORT, which returns the ref to the input list for a more fluent API that allows chained sorts. 
     * @return The input list, sorted in-place.
     * @throws SortingException
     */
    public List<T> sort(List<T> input, String sortField, SortDirection sortDirection, NullOrderingBehavior nullBehavior) throws SortingException {
        if (sortDirection == null) {
            sortDirection = DEFAULT_SORT_DIRECTION;
        }

        if (!comparatorsByField.containsKey(sortField)) {
            throw new SortingException("sortField argument: \"" + sortField + "\" is not a supported fieldName.");
        }

        var comparator = composeComparator(sortField, sortDirection, nullBehavior);

        if (comparator != null && !CollectionUtils.isEmpty(input)) {
            input.sort(comparator);
        }

        return input;
    }

    public List<T> sort(List<T> input, String sortField, SortDirection sortDirection) throws SortingException {
        return sort(input, sortField, sortDirection, null);
    }

    public List<T> sort(List<T> input, String sortField, NullOrderingBehavior nullBehavior) throws SortingException {
        return sort(input, sortField, null, null);
    }

    public List<T> sort(List<T> input, String sortField) throws SortingException {
        return sort(input, sortField, null, null);
    }

    private Comparator<T> composeComparator(String fieldName, SortDirection sortDirection, NullOrderingBehavior nullBehavior) {
        var comparator = comparatorsByField.get(fieldName);

        if (comparator == null) {
            return comparator;
        }

        if (sortDirection == null) {
            sortDirection = defaultSortDirectionsByField.getOrDefault(fieldName, DEFAULT_SORT_DIRECTION);
        }

        comparator = addSortDirection(comparator, sortDirection);

        if (nullBehavior == null) {
            nullBehavior = defaultNullBehaviorsByField.getOrDefault(fieldName, DEFAULT_NULL_BEHAVIOR);
        }

        comparator = addNullBehavior(comparator, nullBehavior);

        return comparator;
    }

    private Comparator<T> addSortDirection(@NonNull Comparator<T> comparator, SortDirection sortDirection) {
        if (sortDirection == SortDirection.DESCENDING) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    private Comparator<T> addNullBehavior(@NonNull Comparator<T> comparator, NullOrderingBehavior nullBehavior) {
        if (nullBehavior == NullOrderingBehavior.NULLS_FIRST) {
            return Comparator.nullsFirst(comparator);
        } else if (nullBehavior == NullOrderingBehavior.NULLS_LAST) {
            return Comparator.nullsLast(comparator);
        } else {
            return comparator;
        }
    }

    private static boolean isValidFieldName(String fieldName) {
        return fieldName == null || SourceVersion.isIdentifier(fieldName);
    }

}
