package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.concurrent.CompletableFuture;

public interface FlorumSporumSoundEvents {

    SoundEvent SPORE_BLOSSOM_CLOSE = of("block.spore_blossom.close");
    SoundEvent SPORE_BLOSSOM_OPEN = of("block.spore_blossom.open");

    private static SoundEvent of(String path) {
        Identifier id = FlorumSporum.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    static void load() {}

    class Provider extends FabricSoundsProvider {

        public Provider(PackOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
            super(output, holderProviderFuture);
        }

        @Override
        protected void configure(HolderLookup.Provider holderProvider, SoundExporter exporter) {
            add(SPORE_BLOSSOM_CLOSE, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, exporter);
            add(SPORE_BLOSSOM_OPEN, SoundEvents.BIG_DRIPLEAF_TILT_UP, exporter);
        }

        private static void add(SoundEvent sporeBlossomEvent, SoundEvent event, SoundExporter exporter) {
            exporter.add(sporeBlossomEvent, SoundTypeBuilder.of()
                .category(SoundSource.BLOCKS)
                .sound(SoundTypeBuilder.EntryBuilder.ofEvent(event))
                .subtitle("subtitles." + sporeBlossomEvent.location().getPath())
            );
        }

        @Override
        public String getName() {
            return "Sounds Provider";
        }

    }

}
