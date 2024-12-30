package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import com.axialeaa.florumsporum.util.FlorumSporumUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if >=1.21.1 {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;*/

import /*$ copy_state_function_import >>*/ net.minecraft.loot.function.CopyStateLootFunction ;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = /*$ mod_id*/ "florum-sporum";

    public static final FabricLoader LOADER = FabricLoader.getInstance();
    public static final ModContainer CONTAINER = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new);

    public static final String MOD_NAME = CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        FlorumSporumSoundEvents.init();
        FlorumSporumBlockTags.init();

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((itemGroup, fabricItemGroupEntries) -> {
            if (!itemGroup.contains(Items.SPORE_BLOSSOM.getDefaultStack()))
                return;

            for (ItemStack itemStack : itemGroup.getDisplayStacks()) {
                if (!itemStack.isOf(Items.SPORE_BLOSSOM))
                    continue;

                ItemStack prevStack = null;

                for (int i = 0; i <= 3; i++) {
                    if (i == 0) {
                        prevStack = FlorumSporumUtils.addDataForAge(itemStack, 0);
                        continue;
                    }

                    ItemStack sporeBlossomStack = FlorumSporumUtils.createSporeBlossomStack(i);
                    fabricItemGroupEntries.addAfter(prevStack, sporeBlossomStack);

                    prevStack = sporeBlossomStack;
                }

                return;
            }
        });

        //? if >=1.20.6 {
        LootTableEvents.MODIFY.register((registryKey, tableBuilder, source /*? if >=1.21.1 >>*/ ,wrapperLookup ) -> {
            if (source.isBuiltin() && registryKey == Blocks.SPORE_BLOSSOM.getLootTableKey() /*? if >=1.21.3 >>*/ .orElseThrow() )
                tableBuilder.modifyPools(builder -> builder.apply(CopyStateLootFunction.builder(Blocks.SPORE_BLOSSOM).addProperty(FlorumSporumUtils.AGE)));
        });
        //?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && id.equals(Blocks.SPORE_BLOSSOM.getLootTableId()))
                tableBuilder.modifyPools(builder -> builder.apply(CopyStateFunction.builder(Blocks.SPORE_BLOSSOM).addProperty(FlorumSporumUtils.AGE)));
        });
        *///?}
    }

    public static Identifier id(String path) {
        return /*$ identifier*/ Identifier.of(MOD_ID, path);
    }

    public static void logRegistryInit(String registry) {
        LOGGER.info("Registering {} {}!", MOD_NAME, registry);
    }

}
