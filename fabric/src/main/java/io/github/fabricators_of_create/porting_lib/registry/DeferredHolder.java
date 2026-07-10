package io.github.fabricators_of_create.porting_lib.registry;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T> {
    protected ResourceKey<R> key;
    private R value;

    protected DeferredHolder(ResourceKey<R> key) {
        this.key = key;
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends net.minecraft.core.Registry<R>> registryKey, ResourceLocation name) {
        @SuppressWarnings("unchecked")
        ResourceKey<R> key = (ResourceKey<R>) ResourceKey.create(registryKey, name);
        return new DeferredHolder<>(key);
    }

    public ResourceKey<R> getKey() {
        return key;
    }

    public ResourceLocation getId() {
        return key != null ? key.location() : null;
    }

    public void bindValue(R value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public T get() {
        if (value == null && key != null) {
            // Registration.register() never calls bindValue(), so look up lazily from built-in registries.
            BuiltInRegistries.REGISTRY.get(key.registry())
                .map(regHolder -> (Registry<R>) regHolder.value())
                .flatMap(reg -> reg.get(key.location()))
                .map(entryHolder -> (T) entryHolder.value())
                .ifPresent(v -> value = v);
        }
        return (T) value;
    }

    @Override
    public boolean isBound() {
        return value != null;
    }

    @Override
    public boolean is(ResourceLocation location) {
        return key != null && key.location().equals(location);
    }

    @Override
    public boolean is(ResourceKey<R> resourceKey) {
        return key != null && key.equals(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return key != null && predicate.test(key);
    }

    @Override
    public boolean is(Holder<R> other) {
        return other.unwrapKey().map(k -> k.equals(key)).orElse(false);
    }

    @Override
    public boolean is(TagKey<R> tag) {
        return false;
    }

    @Override
    public Stream<TagKey<R>> tags() {
        return Stream.empty();
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return Either.left(key);
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return Optional.ofNullable(key);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R value() {
        return value != null ? value : (R) get();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        // Vanilla's Holder/Holder.Reference use identity equality, but this shim is a standalone
        // Holder implementation distinct from the registry's real Holder.Reference for the same key.
        // Compare by key so lookups (e.g. LivingEntity#hasEffect) work regardless of which Holder
        // instance for the same registry entry is used as the map key vs. the search key.
        if (!(other instanceof Holder<?> holder)) return false;
        return key != null && holder.unwrapKey().map(key::equals).orElse(false);
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
