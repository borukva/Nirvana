package net.minecraft;

import com.mojang.datafixers.types.Type;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

// Stub for BlockEntityType$Builder (class_2591$class_2592), removed in MC 1.21.10.
// Loom cannot remap this class so registrate's calls stay at runtime as
// net.minecraft.class_2591$class_2592.method_20528 / method_11034.
// This stub satisfies those calls via FabricBlockEntityTypeBuilder.
public class class_2591$class_2592 {
    private final FabricBlockEntityTypeBuilder<?> delegate;

    private class_2591$class_2592(FabricBlockEntityTypeBuilder<?> delegate) {
        this.delegate = delegate;
    }

    // Was: BlockEntityType.Builder.of(supplier, blocks)
    public static <T extends BlockEntity> class_2591$class_2592 method_20528(
            BlockEntityType.BlockEntitySupplier<? extends T> supplier, Block... blocks) {
        return new class_2591$class_2592(FabricBlockEntityTypeBuilder.create(supplier::create, blocks));
    }

    // Was: builder.build(null)
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityType<T> method_11034(Type<?> type) {
        return (BlockEntityType<T>) delegate.build(type);
    }
}
