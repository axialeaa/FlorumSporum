//? if <1.21.4 {
/*package com.axialeaa.florumsporum.data.model;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.item.SporeBlossomStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class SporeBlossomItemModelJson {

    private static final String
        PREDICATE = "predicate",
        MODEL = "model",
        OVERRIDES = "overrides";

    public static final Identifier PREDICATE_ID = FlorumSporum.id(SporeBlossomStack.AGE_NAME);

    public static JsonObject create() {
        JsonObject root = new JsonObject();
        JsonArray overrides = new JsonArray();

        for (int age = 0; age <= SporeBlossomProperties.MAX_AGE; age++) {
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();

            predicate.addProperty(PREDICATE_ID.toString(), (float) age / SporeBlossomProperties.MAX_AGE);

            override.add(PREDICATE, predicate);
            override.addProperty(MODEL, SporeBlossomModels.getIdWithMaxOpenness(age).toString());

            overrides.add(override);
        }

        root.add(OVERRIDES, overrides);

        return root;
    }

    public static void registerModelPredicateProvider() {
        ClampedModelPredicateProvider provider = (stack, world, entity, seed) -> getModelPredicateDelta(stack);
        ModelPredicateProviderRegistry.register(Items.SPORE_BLOSSOM, PREDICATE_ID, provider);
    }

    private static float getModelPredicateDelta(ItemStack stack) {
        return (float) SporeBlossomStack.getAgeComponentValue(stack) / SporeBlossomProperties.MAX_AGE;
    }

}
*///?}