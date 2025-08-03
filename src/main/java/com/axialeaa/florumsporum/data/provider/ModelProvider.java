package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.data.model.SporeBlossomModels;
import com.axialeaa.florumsporum.mixin.accessor.BlockStateModelGeneratorAccessor;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.item.Items;

public class ModelProvider extends FabricModelProvider {

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @SuppressWarnings("UnreachableCode")
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        SporeBlossomModels.setIds(generator);

        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(Blocks.SPORE_BLOSSOM)
            .with(getBlockStates())
            .coordinate(BlockStateModelGeneratorAccessor.getNorthDefaultRotationOperations())
        );

        generator.itemModelOutput.accept(Items.SPORE_BLOSSOM, SporeBlossomModels.selectItemModel());
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {}

    private static BlockStateVariantMap<WeightedVariant> getBlockStates() {
        BlockStateVariantMap.DoubleProperty<WeightedVariant, Integer, Openness> property = BlockStateVariantMap.models(SporeBlossomProperties.AGE, SporeBlossomProperties.OPENNESS);

        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
            for (Openness openness : Openness.values())
                property.register(age, openness, BlockStateModelGenerator.createWeightedVariant(SporeBlossomModels.getId(age, openness)));
        }

        return property;
    }

}