package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.data.provider.BlockTagProvider;
import com.axialeaa.florumsporum.data.provider.ModelProvider;
import com.axialeaa.florumsporum.data.provider.RecipeProvider;
import com.axialeaa.florumsporum.data.provider.SoundsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FlorumSporumDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModelProvider::new);
        pack.addProvider(SoundsProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(BlockTagProvider::new);
    }

}
