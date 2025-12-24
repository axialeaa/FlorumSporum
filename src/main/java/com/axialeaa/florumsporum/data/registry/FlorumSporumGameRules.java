package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public interface FlorumSporumGameRules {

    GameRuleCategory CATEGORY = GameRuleCategory.register(FlorumSporum.id("config"));

    GameRule<Double> SPORE_BLOSSOM_GROWTH_CHANCE = registerDouble(
        "spore_blossom_growth_chance",
        0.1,
        0.0,
        1.0
    );

    GameRule<Integer> SPORE_BLOSSOM_UNFURL_INTERVAL = registerInt(
        "spore_blossom_unfurl_interval",
        SharedConstants.TICKS_PER_SECOND / 2,
        1,
        Integer.MAX_VALUE
    );

    GameRule<Integer> ENTITY_CHECK_INTERVAL = registerInt(
        "spore_blossom_entity_check_interval",
        SharedConstants.TICKS_PER_SECOND * 3,
        1,
        Integer.MAX_VALUE
    );

    GameRule<Integer> PANDA_DEFAULT_MAX_SNEEZE_INTERVAL = registerInt(
        "panda_default_max_sneeze_interval",
        SharedConstants.TICKS_PER_MINUTE * 5,
        0,
        Integer.MAX_VALUE
    );

    GameRule<Integer> PANDA_WEAK_MAX_SNEEZE_INTERVAL = registerInt(
        "panda_weak_max_sneeze_interval",
        SharedConstants.TICKS_PER_SECOND * 25,
        0,
        Integer.MAX_VALUE
    );

    GameRule<Integer> PANDA_SPORE_SHOWER_MAX_SNEEZE_INTERVAL = registerInt(
        "panda_spore_shower_max_sneeze_interval",
        SharedConstants.TICKS_PER_SECOND * 5,
        0,
        Integer.MAX_VALUE
    );

    GameRule<Integer> PANDA_SPORE_SHOWER_CHECK_DEPTH = registerInt(
        "panda_spore_shower_check_depth",
        8,
        1,
        Integer.MAX_VALUE
    );

    static GameRule<Double> registerDouble(String path, double defaultValue, double minValue, double maxValue) {
        return register(GameRuleBuilder.forDouble(defaultValue).range(minValue, maxValue), path);
    }

    static GameRule<Integer> registerInt(String path, int defaultValue, int minValue, int maxValue) {
        return register(GameRuleBuilder.forInteger(defaultValue).range(minValue, maxValue), path);
    }

    static <T> GameRule<T> register(GameRuleBuilder<T> builder, String path) {
        return builder.category(CATEGORY).buildAndRegister(FlorumSporum.id(path));
    }

    static void load() {}

}
