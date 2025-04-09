package com.axialeaa.florumsporum.data.model;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.axialeaa.florumsporum.block.property.Openness;

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

public class SporeBlossomModels {

    private static final String
        TEMPLATE = "template",
        FLOWER = "flower",
        SPORE_BLOSSOM = "spore_blossom";

    private static final TextureKey FLOWER_TEXTURE_KEY = TextureKey.of(FLOWER);

    private static final Identifier[]
        CLOSED_IDENTIFIERS = new Identifier[SporeBlossomBehaviour.AGE_COUNT],
        AJAR_IDENTIFIERS = new Identifier[SporeBlossomBehaviour.AGE_COUNT],
        PARTIAL_IDENTIFIERS = new Identifier[SporeBlossomBehaviour.AGE_COUNT],
        FULL_IDENTIFIERS = new Identifier[SporeBlossomBehaviour.AGE_COUNT];

    private static final Model
        TEMPLATE_CLOSED = template(Openness.CLOSED),
        TEMPLATE_AJAR = template(Openness.AJAR),
        TEMPLATE_PARTIAL = template(Openness.PARTIAL),
        TEMPLATE_FULL = template(Openness.FULL);

    private static Model template(Openness openness) {
        String suffix = '_' + SPORE_BLOSSOM + '_' + openness.asString();
        String parent = TEMPLATE + suffix;

        return sporeBlossom(parent, Optional.of(suffix));
    }

    private static Model sporeBlossom(String parent, Optional<String> variant) {
        return block(parent, variant, FLOWER_TEXTURE_KEY);
    }

    private static Model block(String parent, Optional<String> variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(FlorumSporum.id("block/" + parent)), variant, requiredTextureKeys);
    }

    public static Identifier getIdWithMaxOpenness(int age) {
        return getId(age, Openness.byOrdinal(age));
    }

    public static Identifier getId(int age, Openness openness) {
        return switch (openness) {
            case CLOSED -> CLOSED_IDENTIFIERS[age];
            case AJAR -> AJAR_IDENTIFIERS[age];
            case PARTIAL -> PARTIAL_IDENTIFIERS[age];
            case FULL -> FULL_IDENTIFIERS[age];
        };
    }

    public static void setIds(BlockStateModelGenerator generator) {
        for (int age = 0; age <= SporeBlossomBehaviour.MAX_AGE; age++) {
            CLOSED_IDENTIFIERS[age] = upload(generator, TEMPLATE_CLOSED, age);
            AJAR_IDENTIFIERS[age] = upload(generator, TEMPLATE_AJAR, age);
            PARTIAL_IDENTIFIERS[age] = upload(generator, TEMPLATE_PARTIAL, age);
            FULL_IDENTIFIERS[age] = upload(generator, TEMPLATE_FULL, age);
        }
    }

    private static Identifier upload(BlockStateModelGenerator generator, Model model, int age) {
        Identifier id = age == SporeBlossomBehaviour.MAX_AGE ? TextureMap.getId(Blocks.SPORE_BLOSSOM) : FlorumSporum.id("block/spore_blossom_stage_" + age);
        TextureMap textureMap = new TextureMap().put(FLOWER_TEXTURE_KEY, id);

        return model.upload(Blocks.SPORE_BLOSSOM, "_stage_" + age, textureMap, generator.modelCollector);
    }

    //? if >=1.21.4 {
    public static ItemModel.Unbaked getItemModelForAge(int age) {
        return ItemModels.basic(getIdWithMaxOpenness(age));
    }

    public static ItemModel.Unbaked selectItemModel() {
        Map<Integer, ItemModel.Unbaked> map = new HashMap<>(SporeBlossomBehaviour.AGE_COUNT);

        for (int age = 0; age <= SporeBlossomBehaviour.MAX_AGE; age++)
            map.put(age, getItemModelForAge(age));

        return ItemModels.select(SporeBlossomBehaviour.AGE, getItemModelForAge(SporeBlossomBehaviour.MAX_AGE), map);
    }
    //?}

}
