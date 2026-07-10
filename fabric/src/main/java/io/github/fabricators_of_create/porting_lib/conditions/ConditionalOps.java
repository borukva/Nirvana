package io.github.fabricators_of_create.porting_lib.conditions;

import com.mojang.serialization.Codec;

public class ConditionalOps {
    public static <T> Codec<java.util.Optional<WithConditions<T>>> createConditionalCodecWithConditions(Codec<T> codec) {
        return Codec.unit(java.util.Optional.empty());
    }
}
