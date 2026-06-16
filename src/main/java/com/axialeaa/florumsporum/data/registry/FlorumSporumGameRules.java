package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class FlorumSporumGameRules {

    public static final GameRuleCategory CATEGORY = GameRuleCategory.register(FlorumSporum.id("config"));

    public static final GameRule<Double> SPORE_BLOSSOM_GROWTH_CHANCE = registerDouble("spore_blossom_growth_chance", 0.1, 0.0, 1.0);
    public static final GameRule<Integer> SPORE_BLOSSOM_UNFURL_INTERVAL = registerInt("spore_blossom_unfurl_interval", SharedConstants.TICKS_PER_SECOND / 2, 1);
    public static final GameRule<Integer> ENTITY_CHECK_INTERVAL = registerInt("spore_blossom_entity_check_interval", SharedConstants.TICKS_PER_SECOND * 3, 1);

    public static GameRule<Double> registerDouble(String path, double defaultValue, double minValue, double maxValue) {
        return register(GameRuleBuilder.forDouble(defaultValue).range(minValue, maxValue), path);
    }

    public static GameRule<Integer> registerInt(String path, int defaultValue, int minValue) {
        return registerInt(path, defaultValue, minValue, Integer.MAX_VALUE);
    }

    public static GameRule<Integer> registerInt(String path, int defaultValue, int minValue, int maxValue) {
        return register(GameRuleBuilder.forInteger(defaultValue).range(minValue, maxValue), path);
    }

    public static <T> GameRule<T> register(GameRuleBuilder<T> builder, String path) {
        return builder.category(CATEGORY).buildAndRegister(FlorumSporum.id(path));
    }

    public static void init() {}

}
