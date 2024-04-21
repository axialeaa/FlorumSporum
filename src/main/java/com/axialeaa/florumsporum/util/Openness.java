package com.axialeaa.florumsporum.util;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum Openness implements StringIdentifiable {

    CLOSED,
    AJAR,
    PARTIAL,
    FULL;

    @Override
    public String asString() {
        return toString().toLowerCase(Locale.ROOT);
    }

    /**
     * @return the openness enum value at the specified ordinal.
     */
    public static Openness get(int ordinal) {
        return values()[ordinal];
    }

}
