package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.concurrent.CompletableFuture;

public class SoundsProvider extends FabricSoundsProvider {

    public SoundsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, SoundExporter exporter) {
        add(exporter, FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN);
        add(exporter, FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_UP);
    }

    private static void add(SoundExporter exporter, SoundEvent sporeBlossomEvent, SoundEvent event) {
        exporter.add(sporeBlossomEvent, SoundTypeBuilder.of()
            .category(SoundCategory.BLOCKS)
            .sound(SoundTypeBuilder.EntryBuilder.ofEvent(event))
            .subtitle("subtitles." + sporeBlossomEvent.id().getPath())
        );
    }

    @Override
    public String getName() {
        return "Sounds Provider";
    }

}
