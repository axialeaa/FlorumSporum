package com.axialeaa.florumsporum.util;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum Openness implements StringIdentifiable {

    CLOSED, AJAR, PARTIAL, FULL;

    @Override
    public String asString() {
        return this.toString().toLowerCase(Locale.ROOT);
    }

    public static Openness byOrdinal(int ordinal) {
        return values()[ordinal];
    }

}
