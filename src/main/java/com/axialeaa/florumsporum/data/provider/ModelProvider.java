package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.data.model.SporeBlossomModels;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

//? if >=1.21.5 {
import com.axialeaa.florumsporum.mixin.accessor.BlockStateModelGeneratorAccessor;
import net.minecraft.client.render.model.json.WeightedVariant;
//?}

//? if >=1.21.4 {
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.*;
//?} else {
/*import net.minecraft.data.client.*;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import com.axialeaa.florumsporum.data.model.SporeBlossomItemModelJson;
*///?}

public class ModelProvider extends FabricModelProvider {

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    //? if >=1.21.5
    @SuppressWarnings("UnreachableCode")
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        SporeBlossomModels.setIds(generator);

        //? if <1.21.5 {
        /*generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(Blocks.SPORE_BLOSSOM)
            .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates())
            .coordinate(getBlockStates())
        );
        *///?} else {
        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(Blocks.SPORE_BLOSSOM)
            .with(getBlockStates())
            .coordinate(BlockStateModelGeneratorAccessor.getNorthDefaultRotationOperations())
        );
        //?}

        //? if >=1.21.4 {
        generator.itemModelOutput.accept(Items.SPORE_BLOSSOM, SporeBlossomModels.selectItemModel());
        //?} else
        /*generator.excludeFromSimpleItemModelGeneration(Blocks.SPORE_BLOSSOM);*/
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        //? if <1.21.4
        /*generator.writer.accept(ModelIds.getItemModelId(Items.SPORE_BLOSSOM), SporeBlossomItemModelJson::create);*/
    }

    //? if <1.21.5 {
    /*private static BlockStateVariantMap getBlockStates() {
        BlockStateVariantMap.DoubleProperty<Integer, Openness> property = BlockStateVariantMap.create(SporeBlossomBehaviour.AGE, SporeBlossomBehaviour.OPENNESS);
        return property.register((age, openness) -> BlockStateVariant.create().put(VariantSettings.MODEL, SporeBlossomModels.getId(age, openness)));
    }
    *///?} else {
    private static BlockStateVariantMap<WeightedVariant> getBlockStates() {
        BlockStateVariantMap.DoubleProperty<WeightedVariant, Integer, Openness> property = BlockStateVariantMap.models(SporeBlossomBehaviour.AGE, SporeBlossomBehaviour.OPENNESS);

        for (int age = 0; age <= SporeBlossomBehaviour.MAX_AGE; age++) {
            for (Openness openness : Openness.values())
                property.register(age, openness, BlockStateModelGenerator.createWeightedVariant(SporeBlossomModels.getId(age, openness)));
        }

        return property;
    }
    //?}

}