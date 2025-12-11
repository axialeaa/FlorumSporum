package com.axialeaa.florumsporum.block.property;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public enum Openness implements StringRepresentable {

    CLOSED, AJAR, PARTIAL, FULL;

    @Override
    @NonNull
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

}
