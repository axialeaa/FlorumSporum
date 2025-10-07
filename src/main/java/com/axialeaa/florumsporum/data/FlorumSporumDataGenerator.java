package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.data.registry.FlorumSporumRecipes;
import com.axialeaa.florumsporum.data.registry.FlorumSporumSoundEvents;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FlorumSporumDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(FlorumSporumSoundEvents.Provider::new);
        pack.addProvider(FlorumSporumBlockTags.Provider::new);
        pack.addProvider(FlorumSporumRecipes.Provider::new);
        pack.addProvider(SporeBlossomModels.Provider::new);
    }

}
