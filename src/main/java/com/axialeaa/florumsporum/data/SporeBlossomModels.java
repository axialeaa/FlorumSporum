package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.property.Openness;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.mixin.accessor.BlockStateModelGeneratorAccessor;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SporeBlossomModels {

    private static final String TEMPLATE_PATH = "template_spore_blossom";
    private static final String FLOWER = "flower";
    private static final TextureKey FLOWER_TEXTURE_KEY = TextureKey.of(FLOWER);
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

    private static void setIds(BlockStateModelGenerator generator) {
        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
            for (Openness openness : Openness.values())
                setId(age, openness, upload(generator, age, openness));
        }
    }

    private static Identifier upload(BlockStateModelGenerator generator, int age, Openness openness) {
        Model model = template(openness);

        Identifier classicId = TextureMap.getId(Blocks.SPORE_BLOSSOM); // returns minecraft:block/spore_blossom
        String texturePath = "spore_blossom_stage_" + age;

        Identifier textureId = age == SporeBlossomProperties.MAX_AGE ? classicId : getPrefixedBlockId(texturePath);
        TextureMap textureMap = new TextureMap().put(FLOWER_TEXTURE_KEY, textureId);

        return model.upload(getPrefixedBlockId(openness.asString() + "/stage_" + age), textureMap, generator.modelCollector);
    }

    private static Model template(Openness openness) {
        String suffix = '_' + openness.asString();
        Identifier id = getPrefixedBlockId(TEMPLATE_PATH + suffix);

        return new Model(Optional.of(id), Optional.of(suffix), FLOWER_TEXTURE_KEY);
    }

    private static Identifier getPrefixedBlockId(String path) {
        return FlorumSporum.id(path).withPrefixedPath("block/");
    }

    private static ItemModel.Unbaked getItemModelForAge(int age) {
        return ItemModels.basic(getIdWithMaxOpenness(age));
    }

    private static ItemModel.Unbaked selectItemModel() {
        Map<Integer, ItemModel.Unbaked> map = new HashMap<>(SporeBlossomProperties.GROWTH_STAGE_COUNT);

        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++)
            map.put(age, getItemModelForAge(age));

        return ItemModels.select(SporeBlossomProperties.AGE, getItemModelForAge(SporeBlossomProperties.MAX_AGE), map);
    }

    public static class Provider extends FabricModelProvider {

        public Provider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator generator) {
            setIds(generator);

            generator.itemModelOutput.accept(Items.SPORE_BLOSSOM, selectItemModel());

            generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(Blocks.SPORE_BLOSSOM)
                .with(getBlockStates())
                .coordinate(BlockStateModelGeneratorAccessor.getNorthDefaultRotationOperations())
            );
        }

        @Override
        public void generateItemModels(ItemModelGenerator generator) {}

        private static BlockStateVariantMap<WeightedVariant> getBlockStates() {
            BlockStateVariantMap.DoubleProperty<WeightedVariant, Integer, Openness> property = BlockStateVariantMap.models(SporeBlossomProperties.AGE, SporeBlossomProperties.OPENNESS);

            for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
                for (Openness openness : Openness.values())
                    property.register(age, openness, BlockStateModelGenerator.createWeightedVariant(getId(age, openness)));
            }

            return property;
        }

    }

}
