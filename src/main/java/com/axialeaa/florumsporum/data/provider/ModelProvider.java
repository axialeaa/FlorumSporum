package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.data.FlorumSporumModels;
import com.axialeaa.florumsporum.util.FlorumSporumUtils;
import com.axialeaa.florumsporum.util.Openness;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

//? if >=1.21.4 {
/*import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.util.Util;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import java.util.HashMap;
*///?} else {
import net.minecraft.data.client.*;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
//?}

public class ModelProvider extends FabricModelProvider {

    private static final Identifier[] CLOSED_IDENTIFIERS = new Identifier[4];
    private static final Identifier[] AJAR_IDENTIFIERS = new Identifier[4];
    private static final Identifier[] PARTIAL_IDENTIFIERS = new Identifier[4];
    private static final Identifier[] FULL_IDENTIFIERS = new Identifier[4];

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        setModelIds(generator);

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(Blocks.SPORE_BLOSSOM)
            .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates())
            .coordinate(getBlockStates())
        );

        //? if >=1.21.4 {
        /*generator.itemModelOutput.accept(Items.SPORE_BLOSSOM, ItemModels.select(
            FlorumSporumUtils.AGE,
            getItemModelForAge(3),
            Util.make(new HashMap<>(4), map -> {
                for (int age = 0; age <= 3; age++)
                    map.put(age, getItemModelForAge(age));
            })
        ));
        *///?} else
        generator.excludeFromSimpleItemModelGeneration(Blocks.SPORE_BLOSSOM);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        //? if <1.21.4 {
        generator.writer.accept(ModelIds.getItemModelId(Items.SPORE_BLOSSOM), () -> {
            JsonObject root = new JsonObject();
            JsonArray overrides = new JsonArray();

            String ageName = FlorumSporumUtils.AGE.getName();

            for (int age = 0; age <= 3; age++) {
                JsonObject override = new JsonObject();
                JsonObject predicate = new JsonObject();

                predicate.addProperty(FlorumSporum.id(ageName).toString(), age / 3.0F);

                override.add("predicate", predicate);
                override.addProperty("model", getId(age).toString());

                overrides.add(override);
            }

            root.add("overrides", overrides);

            return root;
        });
        //?}
    }

    private static BlockStateVariantMap getBlockStates() {
        BlockStateVariantMap.DoubleProperty<Integer, Openness> property = BlockStateVariantMap.create(FlorumSporumUtils.AGE, FlorumSporumUtils.OPENNESS);
        return property.register((age, openness) -> BlockStateVariant.create().put(VariantSettings.MODEL, getId(age, openness)));
    }

    private static Identifier getId(int age, Openness openness) {
        return switch (openness) {
            case CLOSED -> CLOSED_IDENTIFIERS[age];
            case AJAR -> AJAR_IDENTIFIERS[age];
            case PARTIAL -> PARTIAL_IDENTIFIERS[age];
            case FULL -> FULL_IDENTIFIERS[age];
        };
    }

    private static Identifier getId(int age) {
        return getId(age, Openness.byOrdinal(age));
    }

    //? if >=1.21.4 {
    /*private static ItemModel.Unbaked getItemModelForAge(int age) {
        return ItemModels.basic(getId(age));
    }
    *///?}

    private static void setModelIds(BlockStateModelGenerator generator) {
        for (int age = 0; age <= 3; age++) {
            CLOSED_IDENTIFIERS[age] = upload(generator, FlorumSporumModels.TEMPLATE_SPORE_BLOSSOM_CLOSED, age);
            AJAR_IDENTIFIERS[age] = upload(generator, FlorumSporumModels.TEMPLATE_SPORE_BLOSSOM_AJAR, age);
            PARTIAL_IDENTIFIERS[age] = upload(generator, FlorumSporumModels.TEMPLATE_SPORE_BLOSSOM_PARTIAL, age);
            FULL_IDENTIFIERS[age] = upload(generator, FlorumSporumModels.TEMPLATE_SPORE_BLOSSOM_FULL, age);
        }
    }

    private static Identifier upload(BlockStateModelGenerator generator, Model model, int age) {
        Identifier id = age == 3 ? TextureMap.getId(Blocks.SPORE_BLOSSOM) : FlorumSporum.id("block/spore_blossom_stage_" + age);
        TextureMap textureMap = new TextureMap().put(FlorumSporumModels.FLOWER_TEXTURE_KEY, id);

        return model.upload(Blocks.SPORE_BLOSSOM, "_stage_" + age, textureMap, generator.modelCollector);
    }

}