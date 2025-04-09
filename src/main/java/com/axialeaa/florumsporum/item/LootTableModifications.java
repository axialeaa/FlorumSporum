package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;

import /*$ loot_table_event_import >>*/ net.fabricmc.fabric.api.loot.v3.LootTableEvents ;
import net.minecraft.loot.function. /*$ copy_state_function >>*/ CopyStateLootFunction ;

public class LootTableModifications {

    public static void addSporeBlossomAgeFunction() {
        //? if >=1.20.6 {
        LootTableEvents.MODIFY.register((registryKey, tableBuilder, source /*? if >=1.21.1 >>*/ ,wrapperLookup ) -> {
            if (source.isBuiltin() && registryKey.equals(Blocks.SPORE_BLOSSOM.getLootTableKey() /*? if >=1.21.3 >>*/ .orElseThrow() ))
        //?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && id.equals(Blocks.SPORE_BLOSSOM.getLootTableId()))
        *///?}
                modifyPools(tableBuilder);
        });
    }

    private static void modifyPools(LootTable.Builder tableBuilder) {
        tableBuilder.modifyPools(builder -> builder.apply(getAgeStateLootFunction()));
    }

    private static /*$ copy_state_function >>*/ CopyStateLootFunction .Builder getAgeStateLootFunction() {
        return /*$ copy_state_function >>*/ CopyStateLootFunction .builder(Blocks.SPORE_BLOSSOM)
            .addProperty(SporeBlossomBehaviour.AGE);
    }

}