package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableModifications {

    public static void register() {
        LootTableEvents.REPLACE.register(LootTableModifications::registerPandaSneeze);
        LootTableEvents.MODIFY.register(LootTableModifications::registerSporeBlossom);
    }

    private static LootTable registerPandaSneeze(ResourceKey<LootTable> registryKey, LootTable original, LootTableSource source, HolderLookup.Provider holderLookup) {
        if (!validateLootTable(registryKey, BuiltInLootTables.PANDA_SNEEZE, source))
            return null;

        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(Items.SLIME_BALL))
            )
            .build();
    }

    private static void registerSporeBlossom(ResourceKey<LootTable> registryKey, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider holderLookup) {
        if (!validateLootTable(registryKey, Blocks.SPORE_BLOSSOM.getLootTable().orElseThrow(), source))
            return;

        LootItemFunction.Builder lootFunctionBuilder = CopyBlockState.copyState(Blocks.SPORE_BLOSSOM)
            .copy(SporeBlossomProperties.AGE);

        tableBuilder.modifyPools(builder -> builder.apply(lootFunctionBuilder));
    }

    private static boolean validateLootTable(ResourceKey<LootTable> registryKey, ResourceKey<LootTable> validatorKey, LootTableSource source) {
        return source.isBuiltin() && registryKey.equals(validatorKey);
    }


}
