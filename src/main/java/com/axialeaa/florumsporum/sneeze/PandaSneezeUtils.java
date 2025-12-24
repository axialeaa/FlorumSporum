package com.axialeaa.florumsporum.sneeze;

import com.axialeaa.florumsporum.block.SporeBlossomBehavior;
import com.axialeaa.florumsporum.data.registry.FlorumSporumGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

public class PandaSneezeUtils {

    private static int getSneezeTicks(ServerLevel serverLevel, Panda panda, boolean inSporeShower) {
        GameRules gameRules = serverLevel.getGameRules();

        if (inSporeShower)
            return gameRules.get(FlorumSporumGameRules.PANDA_SPORE_SHOWER_MAX_SNEEZE_INTERVAL);

        return gameRules.get(panda.isWeak() ? FlorumSporumGameRules.PANDA_WEAK_MAX_SNEEZE_INTERVAL : FlorumSporumGameRules.PANDA_DEFAULT_MAX_SNEEZE_INTERVAL);
    }

    public static boolean canSneeze(ServerLevel serverLevel, Panda panda, BlockPos pos, RandomSource random) {
        if (!panda.isBaby() || !panda.canPerformAction())
            return false;

        boolean inSporeShower = isInSporeShower(serverLevel, pos);
        int sneezeTicks = getSneezeTicks(serverLevel, panda, inSporeShower);

        return sneezeTicks > 0 && random.nextInt(sneezeTicks) == 0;
    }

    private static boolean isInSporeShower(ServerLevel serverLevel, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        BlockState blockState = serverLevel.getBlockState(mutable);

        int i = 0;
        int checkRange = serverLevel.getGameRules().get(FlorumSporumGameRules.PANDA_SPORE_SHOWER_CHECK_DEPTH);

        while (i < checkRange && blockState.getCollisionShape(serverLevel, mutable).isEmpty()) {
            mutable.move(Direction.UP);
            i++;

            if (serverLevel.isInWorldBounds(mutable))
                return false;

            blockState = serverLevel.getBlockState(mutable);

            if (blockState.getBlock() instanceof SporeBlossomBlock && SporeBlossomBehavior.canShower(blockState))
                return true;
        }

        return false;
    }

}