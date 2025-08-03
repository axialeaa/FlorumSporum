package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyStateLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public class LootTableModifications {

    public static void register() {
        LootTableEvents.REPLACE.register(LootTableModifications::registerPandaSneeze);
        LootTableEvents.MODIFY.register(LootTableModifications::registerSporeBlossom);
    }

    private static LootTable registerPandaSneeze(RegistryKey<LootTable> registryKey, LootTable original, LootTableSource source, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!validateLootTable(registryKey, LootTables.PANDA_SNEEZE_GAMEPLAY, source))
            return original;

        return LootTable.builder()
            .pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1.0F))
                .with(ItemEntry.builder(Items.SLIME_BALL))
            )
            .build();
    }

    private static void registerSporeBlossom(RegistryKey<LootTable> registryKey, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!validateLootTable(registryKey, Blocks.SPORE_BLOSSOM.getLootTableKey().orElseThrow(), source))
            return;

        LootFunction.Builder lootFunctionBuilder = CopyStateLootFunction.builder(Blocks.SPORE_BLOSSOM)
            .addProperty(SporeBlossomProperties.AGE);

        tableBuilder.modifyPools(builder -> builder.apply(lootFunctionBuilder));
    }

    private static boolean validateLootTable(RegistryKey<LootTable> registryKey, RegistryKey<LootTable> validatorKey, LootTableSource source) {
        return source.isBuiltin() && registryKey.equals(validatorKey);
    }


}
