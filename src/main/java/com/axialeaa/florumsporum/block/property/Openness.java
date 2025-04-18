package com.axialeaa.florumsporum.block.property;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum Openness implements StringIdentifiable {

    CLOSED, AJAR, PARTIAL, FULL;

    @Override
    public String asString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

}
