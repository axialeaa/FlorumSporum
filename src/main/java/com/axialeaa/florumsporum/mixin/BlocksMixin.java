package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.util.SporeBlossomUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import /*$ modify_settings_import*/ net.minecraft.block.MapColor;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Redirect(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=spore_blossom")), at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;" + /*$ modify_settings_target >>*/ "mapColor(Lnet/minecraft/block/MapColor;)" + "Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0))
    private static AbstractBlock.Settings modifySettings(
        //? if >=1.20.1 {
        AbstractBlock.Settings instance, MapColor color) {
        return instance.mapColor(SporeBlossomUtils.MAP_COLOR).ticksRandomly();
        //?} else {
        /*Material material) {
        return AbstractBlock.Settings.of(material, SporeBlossomUtils.MAP_COLOR).ticksRandomly();
        *///?}
    }

}