package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.data.SporeBlossomModels;
import com.axialeaa.florumsporum.mixin.accessor.BlockModelGeneratorsAccessor;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FlorumSporumModelProvider extends FabricModelProvider {

    public FlorumSporumModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        SporeBlossomModels.setIds(generators);

        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(Blocks.SPORE_BLOSSOM)
            .with(getBlockStates())
            .with(BlockModelGeneratorsAccessor.getRotationFacing())
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        TextureMapping textureMapping = TextureMapping.layer0(new Material(FlorumSporum.id("block/spore_blossom_stage_0")));
        Identifier identifier = ModelTemplates.FLAT_ITEM.create(FlorumSporum.id("item/spore_blossom"), textureMapping, generators.modelOutput);

        generators.itemModelOutput.accept(Items.SPORE_BLOSSOM, ItemModelUtils.plainModel(identifier));
    }

    private static PropertyDispatch<MultiVariant> getBlockStates() {
        PropertyDispatch.C2<MultiVariant, Integer, Openness> property = PropertyDispatch.initial(SporeBlossomProperties.AGE, SporeBlossomProperties.OPENNESS);

        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
            for (Openness openness : Openness.values())
                property.select(age, openness, BlockModelGenerators.plainVariant(SporeBlossomModels.getId(age, openness)));
        }

        return property;
    }

}