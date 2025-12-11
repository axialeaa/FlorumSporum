package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.property.Openness;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.mixin.accessor.BlockModelGeneratorsAccessor;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SporeBlossomModels {

    private static final String TEMPLATE_PATH = "template_spore_blossom";
    private static final String FLOWER = "flower";
    private static final TextureSlot FLOWER_TEXTURE_KEY = TextureSlot.create(FLOWER);
    private static final int IDENTIFIER_COUNT = SporeBlossomProperties.GROWTH_STAGE_COUNT * Openness.values().length;
    private static final Identifier[] IDENTIFIERS = new Identifier[IDENTIFIER_COUNT];

    private static Identifier getIdWithMaxOpenness(int age) {
        return getId(age, Openness.values()[age]);
    }

    private static Identifier getId(int age, Openness openness) {
        return IDENTIFIERS[getIdArrayIndex(age, openness)];
    }

    private static void setId(int age, Openness openness, Identifier id) {
        IDENTIFIERS[getIdArrayIndex(age, openness)] = id;
    }

    private static int getIdArrayIndex(int age, Openness openness) {
        return age * SporeBlossomProperties.GROWTH_STAGE_COUNT + openness.ordinal();
    }

    private static void setIds(BlockModelGenerators generators) {
        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
            for (Openness openness : Openness.values())
                setId(age, openness, upload(generators, age, openness));
        }
    }

    private static Identifier upload(BlockModelGenerators generators, int age, Openness openness) {
        ModelTemplate model = template(openness);

        Identifier classicId = TextureMapping.getBlockTexture(Blocks.SPORE_BLOSSOM); // returns minecraft:block/spore_blossom
        String texturePath = "spore_blossom_stage_" + age;

        Identifier textureId = age == SporeBlossomProperties.MAX_AGE ? classicId : getPrefixedBlockId(texturePath);
        TextureMapping textureMap = new TextureMapping().put(FLOWER_TEXTURE_KEY, textureId);

        return model.create(getPrefixedBlockId(openness.getSerializedName() + "/stage_" + age), textureMap, generators.modelOutput);
    }

    private static ModelTemplate template(Openness openness) {
        String suffix = '_' + openness.getSerializedName();
        Identifier id = getPrefixedBlockId(TEMPLATE_PATH + suffix);

        return new ModelTemplate(Optional.of(id), Optional.of(suffix), FLOWER_TEXTURE_KEY);
    }

    private static Identifier getPrefixedBlockId(String path) {
        return FlorumSporum.id(path).withPrefix("block/");
    }

    private static ItemModel.Unbaked getItemModelForAge(int age) {
        return ItemModelUtils.plainModel(getIdWithMaxOpenness(age));
    }

    private static ItemModel.Unbaked selectItemModel() {
        Map<Integer, ItemModel.Unbaked> map = new HashMap<>(SporeBlossomProperties.GROWTH_STAGE_COUNT);

        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++)
            map.put(age, getItemModelForAge(age));

        return ItemModelUtils.selectBlockItemProperty(SporeBlossomProperties.AGE, getItemModelForAge(SporeBlossomProperties.MAX_AGE), map);
    }

    public static class Provider extends FabricModelProvider {

        public Provider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators generators) {
            setIds(generators);

            generators.itemModelOutput.accept(Items.SPORE_BLOSSOM, selectItemModel());

            generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(Blocks.SPORE_BLOSSOM)
                .with(getBlockStates())
                .with(BlockModelGeneratorsAccessor.getRotationFacing())
            );
        }

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {}

        private static PropertyDispatch<MultiVariant> getBlockStates() {
            PropertyDispatch.C2<MultiVariant, Integer, Openness> property = PropertyDispatch.initial(SporeBlossomProperties.AGE, SporeBlossomProperties.OPENNESS);

            for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
                for (Openness openness : Openness.values())
                    property.select(age, openness, BlockModelGenerators.plainVariant(getId(age, openness)));
            }

            return property;
        }

    }

}
