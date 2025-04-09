package com.axialeaa.florumsporum.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;

//? if >=1.21.4 {
import net.minecraft.client.data.BlockStateModelGenerator;
//?} else
/*import net.minecraft.data.client.BlockStateModelGenerator;*/

//? if >=1.21.5 {
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.render.model.json.ModelVariantOperator;
//?}

@Mixin(BlockStateModelGenerator.class)
public interface BlockStateModelGeneratorAccessor {

    //? if >=1.21.5 {
    @Accessor("NORTH_DEFAULT_ROTATION_OPERATIONS")
    static BlockStateVariantMap<ModelVariantOperator> getNorthDefaultRotationOperations() {
        throw new AssertionError();
    }
    //?}

}