package com.axialeaa.florumsporum.mixin.accessor;

import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStateModelGenerator.class)
public interface BlockStateModelGeneratorAccessor {

    @Accessor("NORTH_DEFAULT_ROTATION_OPERATIONS")
    static BlockStateVariantMap<ModelVariantOperator> getNorthDefaultRotationOperations() {
        throw new AssertionError();
    }

}