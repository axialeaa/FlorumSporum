package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.util.Openness;

import java.util.Optional;

//? if >=1.21.4 {
/*import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
*///?} else {
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
//?}

public class FlorumSporumModels {

    public static final TextureKey FLOWER_TEXTURE_KEY = TextureKey.of("flower");

    public static final Model TEMPLATE_SPORE_BLOSSOM_CLOSED = template(Openness.CLOSED);
    public static final Model TEMPLATE_SPORE_BLOSSOM_AJAR = template(Openness.AJAR);
    public static final Model TEMPLATE_SPORE_BLOSSOM_PARTIAL = template(Openness.PARTIAL);
    public static final Model TEMPLATE_SPORE_BLOSSOM_FULL = template(Openness.FULL);

    private static Model template(Openness openness) {
        String suffix = "_" + openness.asString();
        String path = "template_spore_blossom" + suffix;

        return block(path, Optional.of(suffix), FLOWER_TEXTURE_KEY);
    }

    private static Model block(String parent, Optional<String> variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(FlorumSporum.id("block/" + parent)), variant, requiredTextureKeys);
    }

}
