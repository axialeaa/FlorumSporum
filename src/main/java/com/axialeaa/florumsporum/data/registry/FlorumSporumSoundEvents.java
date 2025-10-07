package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public interface FlorumSporumSoundEvents {

    SoundEvent SPORE_BLOSSOM_CLOSE = of("block.spore_blossom.close");
    SoundEvent SPORE_BLOSSOM_OPEN = of("block.spore_blossom.open");

    private static SoundEvent of(String path) {
        Identifier id = FlorumSporum.id(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    static void load() {}

    class Provider extends FabricSoundsProvider {

        public Provider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, SoundExporter exporter) {
            add(SPORE_BLOSSOM_CLOSE, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN, exporter);
            add(SPORE_BLOSSOM_OPEN, SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_UP, exporter);
        }

        private static void add(SoundEvent sporeBlossomEvent, SoundEvent event, SoundExporter exporter) {
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

}
