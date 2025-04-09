package com.axialeaa.florumsporum.mixin.tooltip;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.axialeaa.florumsporum.item.SporeBlossomStack.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract ItemStack copy();

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = /*$ append_tooltip_target >>*/ "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;Ljava/util/function/Consumer;)V" ))
    private void addSporeBlossomTooltip(CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        addTooltip(list, this.copy());
    }

}
