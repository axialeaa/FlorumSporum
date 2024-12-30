package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.util.Identifiers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

import net.minecraft.text.Text;

//? <1.21.4 {
import com.axialeaa.florumsporum.util.FlorumSporumUtils;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Items;
//?}

//? if <1.20.6 {
/*import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
*///?} elif <1.21.4 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
//?}

public class FlorumSporumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerPack(Identifiers.UPSCALE_PACK);

        //? if <1.21.4 {
        String ageName = FlorumSporumUtils.AGE.getName();

        ModelPredicateProviderRegistry.register(Items.SPORE_BLOSSOM, FlorumSporum.id(ageName), (stack, world, entity, seed) -> {
            //? if <1.20.6 {
            /*NbtCompound compound = stack.getSubNbt("BlockStateTag");

            if (compound == null)
                return 1.0F;

            NbtElement element = compound.get(ageName);

            try {
                if (element != null)
                    return Integer.parseInt(element.asString()) / 3.0F;
            }
            catch (NumberFormatException ignored) {}

            return 1.0F;
            *///?} else {
            BlockStateComponent component = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
            Integer integer = component.getValue(FlorumSporumUtils.AGE);

            return integer == null ? 1.0F : integer / 3.0F;
            //?}
        });
        //?}
    }

    private static void registerPack(Identifier id) {
        Text translated = Text.translatable("resourcePack.%s.name".formatted(id));
        ResourceManagerHelper.registerBuiltinResourcePack(id, FlorumSporum.CONTAINER, translated, ResourcePackActivationType.NORMAL);

        FlorumSporum.LOGGER.info("Registered pack {}!", id);
    }

}