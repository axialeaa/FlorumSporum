package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.data.registry.FlorumSporumSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class FlorumSporumSoundsProvider extends FabricSoundsProvider {

    public FlorumSporumSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
        super(output, holderProviderFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider holderProvider, SoundExporter exporter) {
        add(FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, exporter);
        add(FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN, SoundEvents.BIG_DRIPLEAF_TILT_UP, exporter);
    }

    private static void add(SoundEvent sporeBlossomEvent, SoundEvent event, SoundExporter exporter) {
        exporter.add(sporeBlossomEvent, SoundTypeBuilder.of()
            .sound(SoundTypeBuilder.RegistrationBuilder.ofEvent(event))
            .subtitle("subtitles." + sporeBlossomEvent.location().getPath())
        );
    }

    @Override
    public String getName() {
        return "Sounds Provider";
    }

}