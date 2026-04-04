package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.data.provider.FlorumSporumBlockTagProvider;
import com.axialeaa.florumsporum.data.provider.FlorumSporumModelProvider;
import com.axialeaa.florumsporum.data.provider.FlorumSporumRecipeProvider;
import com.axialeaa.florumsporum.data.provider.FlorumSporumSoundsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FlorumSporumDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(FlorumSporumSoundsProvider::new);
        pack.addProvider(FlorumSporumBlockTagProvider::new);
        pack.addProvider(FlorumSporumRecipeProvider::new);
        pack.addProvider(FlorumSporumModelProvider::new);
    }

}
