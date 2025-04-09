package com.axialeaa.florumsporum.block.property;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum Openness implements StringIdentifiable {

    CLOSED, AJAR, PARTIAL, FULL;

    @Override
    public String asString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static Openness byOrdinal(int age) {
        return values()[age];
    }

    public static Openness byAge(BlockState state) {
        return byOrdinal(SporeBlossomBehaviour.getAge(state));
    }

}
