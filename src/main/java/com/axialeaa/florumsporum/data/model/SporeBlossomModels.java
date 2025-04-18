package com.axialeaa.florumsporum.data.model;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.property.Openness;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

import java.util.Optional;

//? if >=1.21.4 {
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;

import java.util.HashMap;
import java.util.Map;
//?} else {
/*import net.minecraft.data.client.*;
*///?}

import static com.axialeaa.florumsporum.block.property.SporeBlossomProperties.GROWTH_STAGE_COUNT;

public class SporeBlossomModels {

    private static final TextureKey FLOWER_TEXTURE_KEY = TextureKey.of("flower");
    private static final int IDENTIFIER_COUNT = GROWTH_STAGE_COUNT * Openness.values().length;
    private static final Identifier[] IDENTIFIERS = new Identifier[IDENTIFIER_COUNT];

    public static Identifier getIdWithMaxOpenness(int age) {
        return getId(age, Openness.values()[age]);
    }

    public static Identifier getId(int age, Openness openness) {
        return IDENTIFIERS[getIdArrayIndex(age, openness)];
    }

    public static void setIds(BlockStateModelGenerator generator) {
        for (int age = 0; age < GROWTH_STAGE_COUNT; age++) {
            for (Openness openness : Openness.values())
                IDENTIFIERS[getIdArrayIndex(age, openness)] = upload(generator, age, openness);
        }
    }

    private static int getIdArrayIndex(int age, Openness openness) {
        return age * GROWTH_STAGE_COUNT + openness.ordinal();
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
        String path = "template_spore_blossom" + suffix;

        Identifier id = getPrefixedBlockId(path);

        return new Model(Optional.of(id), Optional.of(suffix), FLOWER_TEXTURE_KEY);
    }

    private static Identifier getPrefixedBlockId(String path) {
        return FlorumSporum.id(path).withPrefixedPath("block/");
    }

    //? if >=1.21.4 {
    public static ItemModel.Unbaked getItemModelForAge(int age) {
        return ItemModels.basic(getIdWithMaxOpenness(age));
    }

    public static ItemModel.Unbaked selectItemModel() {
        Map<Integer, ItemModel.Unbaked> map = new HashMap<>(GROWTH_STAGE_COUNT);

        for (int age = 0; age < GROWTH_STAGE_COUNT; age++)
            map.put(age, getItemModelForAge(age));

        return ItemModels.select(SporeBlossomProperties.AGE, getItemModelForAge(SporeBlossomProperties.MAX_AGE), map);
    }
    //?}

}
