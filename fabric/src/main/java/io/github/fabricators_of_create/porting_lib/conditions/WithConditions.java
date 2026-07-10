package io.github.fabricators_of_create.porting_lib.conditions;

import java.util.List;

public class WithConditions<T> {
    private final List<ICondition> conditions;
    private final T value;

    public WithConditions(List<ICondition> conditions, T value) {
        this.conditions = conditions;
        this.value = value;
    }

    public T carrier() {
        return value;
    }
}
