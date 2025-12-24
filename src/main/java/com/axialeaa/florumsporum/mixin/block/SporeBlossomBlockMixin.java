package com.axialeaa.florumsporum.mixin.block;

import com.axialeaa.florumsporum.block.SporeBlossomBehavior;
import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.data.registry.FlorumSporumGameRules;
import com.axialeaa.florumsporum.item.SporeBlossomStack;
import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.particle.RaycastedSporeArea;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static com.axialeaa.florumsporum.block.property.SporeBlossomProperties.*;

@Mixin(SporeBlossomBlock.class)
public abstract class SporeBlossomBlockMixin extends BlockImplMixin {

    @Shadow @Final private static VoxelShape SHAPE;
    @Unique private static final Map<Direction, VoxelShape> VOXEL_SHAPE_MAP = SporeBlossomBehavior.getShapeMap(SHAPE);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void registerDefaultState(BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.DOWN)
            .setValue(AGE, SporeBlossomProperties.MAX_AGE)
            .setValue(OPENNESS, Openness.FULL)
        );
    }

    @WrapWithCondition(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private boolean shouldAddShowerParticles(Level instance, ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i, @Local(argsOnly = true) BlockState state) {
        return SporeBlossomBehavior.canShower(state);
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 14))
    public int addSporeArea(int original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) RandomSource randomSource) {
        if (isClosed(state))
            return 0;

        RaycastedSporeArea sporeArea = new RaycastedSporeArea(state, pos);

        int count = Mth.lerpInt((float) SporeBlossomProperties.getAge(state) / SporeBlossomProperties.MAX_AGE, 0, original);
        sporeArea.addClientParticles(level, randomSource, count);

        return 0;
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;canSupportCenter(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
    private boolean isValidPlacementSurface(LevelReader levelReader, BlockPos _blockPos, Direction direction, Operation<Boolean> original, @Local(argsOnly = true) BlockPos blockPos, @Local(argsOnly = true) BlockState state) {
        Direction facing = SporeBlossomProperties.getFacing(state);
        return original.call(levelReader, blockPos.relative(facing.getOpposite()), facing);
    }

    @ModifyExpressionValue(method = "updateShape", at = @At(value = "FIELD", target = "Lnet/minecraft/core/Direction;UP:Lnet/minecraft/core/Direction;", opcode = Opcodes.GETSTATIC))
    private Direction modifyUpdateCheckDirection(Direction original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return getFacing(state).getOpposite();
    }

    @ModifyReturnValue(method = "getShape", at = @At("RETURN"))
    private VoxelShape modifyShape(VoxelShape original, @Local(argsOnly = true) BlockState state) {
        return VOXEL_SHAPE_MAP.get(getFacing(state));
    }

    @Override
    public void setPlacedByImpl(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack, Operation<Void> original) {
        super.setPlacedByImpl(level, blockPos, blockState, livingEntity, itemStack, original);

        if (level instanceof ServerLevel serverLevel)
            serverLevel.setBlock(blockPos, SporeBlossomBehavior.open(blockState), Block.UPDATE_CLIENTS);
    }

    @Override
    public ItemStack getCloneItemStackImpl(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl, Operation<ItemStack> original) {
        return SporeBlossomStack.create(blockState);
    }

    @Override
    public void entityInsideImpl(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl, Operation<Void> original) {
        super.entityInsideImpl(blockState, level, blockPos, entity, insideBlockEffectApplier, bl, original);

        if (!(level instanceof ServerLevel serverLevel) || serverLevel.hasNeighborSignal(blockPos) || SporeBlossomProperties.isClosed(blockState))
            return;

        serverLevel.setBlockAndUpdate(blockPos, SporeBlossomBehavior.recoilNoisily(serverLevel, blockPos, blockState));
        serverLevel.scheduleTick(blockPos, this.asBlock(), serverLevel.getGameRules().get(FlorumSporumGameRules.ENTITY_CHECK_INTERVAL));
    }

    @Override
    public void neighborChangedImpl(BlockState blockState, Level level, BlockPos blockPos, Block block, Orientation orientation, boolean bl, Operation<Void> original) {
        super.neighborChangedImpl(blockState, level, blockPos, block, orientation, bl, original);

        if (!(level instanceof ServerLevel serverLevel) || isFullyOpen(blockState))
            return;

        if (serverLevel.hasNeighborSignal(blockPos) || SporeBlossomBehavior.isInvalid(blockState))
            serverLevel.setBlockAndUpdate(blockPos, SporeBlossomBehavior.openNoisily(serverLevel, blockPos, blockState));
    }

    @Override
    public void tickImpl(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, Operation<Void> original) {
        GameRules gameRules = serverLevel.getGameRules();

        if (SporeBlossomBehavior.hasEntityAt(serverLevel, blockPos))
            serverLevel.scheduleTick(blockPos, this.asBlock(), gameRules.get(FlorumSporumGameRules.ENTITY_CHECK_INTERVAL));
        else if (!isFullyOpen(blockState)) {
            serverLevel.setBlockAndUpdate(blockPos, SporeBlossomBehavior.unfurlNoisily(serverLevel, blockPos, blockState));
            serverLevel.scheduleTick(blockPos, this.asBlock(), gameRules.get(FlorumSporumGameRules.SPORE_BLOSSOM_UNFURL_INTERVAL));
        }

        super.tickImpl(blockState, serverLevel, blockPos, randomSource, original);
    }

    @Override
    public BlockState getStateForPlacementImpl(BlockPlaceContext blockPlaceContext, Operation<BlockState> original) {
        BlockState blockState = this.defaultBlockState().setValue(FACING, blockPlaceContext.getClickedFace());
        return blockState.canSurvive(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos()) ? blockState : null;
    }

    @Override
    public boolean isRandomlyTickingImpl(BlockState state, Operation<Boolean> original) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTickImpl(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, Operation<Void> original) {
        super.randomTickImpl(blockState, serverLevel, blockPos, randomSource, original);
        double chance = serverLevel.getGameRules().get(FlorumSporumGameRules.SPORE_BLOSSOM_GROWTH_CHANCE);

        if (chance == 0 || randomSource.nextDouble() > chance)
            return;

        Direction support = getFacing(blockState).getOpposite();
        BlockState supportState = serverLevel.getBlockState(blockPos.relative(support));

        if (supportState.is(FlorumSporumBlockTags.SPORE_BLOSSOM_CAN_GROW_ON))
            serverLevel.setBlockAndUpdate(blockPos, SporeBlossomBehavior.advanceAge(serverLevel, blockPos, blockState));
    }

    @Override
    public void createBlockStateDefinitionImpl(StateDefinition.Builder<Block, BlockState> builder, Operation<Void> original) {
        builder.add(FACING, AGE, OPENNESS);
        super.createBlockStateDefinitionImpl(builder, original);
    }

}
