package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.property.Openness;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public final class SporeBlossomModels {

    private static final String TEMPLATE_PATH = "template_spore_blossom";
    private static final String FLOWER = "flower";
    private static final TextureSlot FLOWER_TEXTURE_KEY = TextureSlot.create(FLOWER);

    private static final int IDENTIFIER_COUNT = SporeBlossomProperties.GROWTH_STAGE_COUNT * Openness.values().length;
    private static final Identifier[] IDENTIFIERS = new Identifier[IDENTIFIER_COUNT];

    public static Identifier getId(int age, Openness openness) {
        return IDENTIFIERS[getIdArrayIndex(age, openness)];
    }

    private static void setId(int age, Openness openness, Identifier id) {
        IDENTIFIERS[getIdArrayIndex(age, openness)] = id;
    }

    private static int getIdArrayIndex(int age, Openness openness) {
        return age * SporeBlossomProperties.GROWTH_STAGE_COUNT + openness.ordinal();
    }

    public static void setIds(BlockModelGenerators generators) {
        for (int age = 0; age < SporeBlossomProperties.GROWTH_STAGE_COUNT; age++) {
            for (Openness openness : Openness.values())
                setId(age, openness, upload(generators, age, openness));
        }
    }

    private static Identifier upload(BlockModelGenerators generators, int age, Openness openness) {
        ModelTemplate model = template(openness);

        Identifier classicId = TextureMapping.getBlockTexture(Blocks.SPORE_BLOSSOM).sprite(); // returns minecraft:block/spore_blossom
        String texturePath = "spore_blossom_stage_" + age;

        Identifier textureId = age == SporeBlossomProperties.MAX_AGE ? classicId : getPrefixedBlockId(texturePath);
        TextureMapping textureMap = new TextureMapping().put(FLOWER_TEXTURE_KEY, new Material(textureId));

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

}
