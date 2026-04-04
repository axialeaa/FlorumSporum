package com.axialeaa.florumsporum.mixin.block;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.data.registry.FlorumSporumGameRules;
import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.particle.RaycastedSporeArea;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
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
import net.minecraft.world.level.block.BonemealableBlock;
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

@SuppressWarnings({ "UnresolvedMixinReference", "MixinAnnotationTarget" })
@Mixin(value = SporeBlossomBlock.class, priority = 1500)
public abstract class SporeBlossomBlockMixin extends BlockImplMixin implements BonemealableBlock {

    @Shadow @Final private static VoxelShape SHAPE;
    @Unique private static final Map<Direction, VoxelShape> VOXEL_SHAPE_MAP = SporeBlossomBehaviour.getShapeMap(SHAPE);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void registerDefaultState(BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.DOWN)
            .setValue(AGE, MAX_AGE)
            .setValue(OPENNESS, Openness.FULL)
        );
    }

    @WrapWithCondition(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    private boolean shouldAddShowerParticles(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, @Local(argsOnly = true, name = "state") BlockState state) {
        return SporeBlossomBehaviour.canShower(state);
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 14))
    public int addSporeArea(int original, BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!isClosed(state)) {
            RaycastedSporeArea sporeArea = new RaycastedSporeArea(state, pos);
            int count = Mth.lerpInt((float) getAge(state) / MAX_AGE, 0, original);

            sporeArea.addClientParticles(level, random, count);
        }

        return 0;
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;canSupportCenter(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
    private boolean isValidPlacementSurface(LevelReader level, BlockPos belowPos, Direction direction, Operation<Boolean> original, @Local(argsOnly = true, name = "pos") BlockPos pos, @Local(argsOnly = true, name = "state") BlockState state) {
        Direction facing = getFacing(state);
        return original.call(level, pos.relative(facing.getOpposite()), facing);
    }

    @ModifyExpressionValue(method = "updateShape", at = @At(value = "FIELD", target = "Lnet/minecraft/core/Direction;UP:Lnet/minecraft/core/Direction;", opcode = Opcodes.GETSTATIC))
    private Direction modifyUpdateCheckDirection(Direction original, @Local(argsOnly = true, name = "state") BlockState state) {
        return getFacing(state).getOpposite();
    }

    @ModifyReturnValue(method = "getShape", at = @At("RETURN"))
    private VoxelShape modifyShape(VoxelShape original, @Local(argsOnly = true, name = "state") BlockState state) {
        return VOXEL_SHAPE_MAP.get(getFacing(state));
    }

    @WrapMethod(method = "isValidBonemealTarget")
    private boolean wrapIsValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, Operation<Boolean> original) {
        return true;
    }

    @WrapMethod(method = "isBonemealSuccess")
    private boolean wrapIsBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state, Operation<Boolean> original) {
        return true;
    }

    @WrapMethod(method = "performBonemeal")
    private void wrapPerformBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, Operation<Void> original) {
        original.call(level, random, pos, state);
        SporeBlossomBehaviour.onFertilized(level, pos, state);
    }

    @Override
    public void setPlacedByImpl(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack, Operation<Void> original) {
        super.setPlacedByImpl(level, pos, state, by, itemStack, original);

        if (level instanceof ServerLevel serverLevel)
            serverLevel.setBlock(pos, SporeBlossomBehaviour.open(state), Block.UPDATE_CLIENTS);
    }

    @Override
    public void entityInsideImpl(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise, Operation<Void> original) {
        super.entityInsideImpl(state, level, pos, entity, effectApplier, isPrecise, original);

        if (level instanceof ServerLevel serverLevel && !serverLevel.hasNeighborSignal(pos) && !isClosed(state)) {
            serverLevel.setBlockAndUpdate(pos, SporeBlossomBehaviour.recoilNoisily(serverLevel, pos, state));
            serverLevel.scheduleTick(pos, this.asBlock(), serverLevel.getGameRules().get(FlorumSporumGameRules.ENTITY_CHECK_INTERVAL));
        }
    }

    @Override
    public void neighborChangedImpl(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston, Operation<Void> original) {
        super.neighborChangedImpl(state, level, pos, block, orientation, movedByPiston, original);

        if (!(level instanceof ServerLevel serverLevel) || isFullyOpen(state))
            return;

        if (serverLevel.hasNeighborSignal(pos) || SporeBlossomBehaviour.isInvalid(state))
            serverLevel.setBlockAndUpdate(pos, SporeBlossomBehaviour.openNoisily(serverLevel, pos, state));
    }

    @Override
    public void tickImpl(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, Operation<Void> original) {
        GameRules gameRules = level.getGameRules();

        if (SporeBlossomBehaviour.hasEntityAt(level, pos))
            level.scheduleTick(pos, this.asBlock(), gameRules.get(FlorumSporumGameRules.ENTITY_CHECK_INTERVAL));
        else if (!isFullyOpen(state)) {
            level.setBlockAndUpdate(pos, SporeBlossomBehaviour.unfurlNoisily(level, pos, state));
            level.scheduleTick(pos, this.asBlock(), gameRules.get(FlorumSporumGameRules.SPORE_BLOSSOM_UNFURL_INTERVAL));
        }

        super.tickImpl(state, level, pos, random, original);
    }

    @Override
    public BlockState getStateForPlacementImpl(BlockPlaceContext context, Operation<BlockState> original) {
        BlockState blockState = super.getStateForPlacementImpl(context, original);

        if (blockState == null)
            return null;

        blockState = this.defaultBlockState()
            .setValue(FACING, context.getClickedFace())
            .setValue(AGE, 0)
            .setValue(OPENNESS, Openness.CLOSED);

        return blockState.canSurvive(context.getLevel(), context.getClickedPos()) ? blockState : null;
    }

    @Override
    public boolean isRandomlyTickingImpl(BlockState state, Operation<Boolean> original) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTickImpl(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, Operation<Void> original) {
        super.randomTickImpl(state, level, pos, random, original);
        double chance = level.getGameRules().get(FlorumSporumGameRules.SPORE_BLOSSOM_GROWTH_CHANCE);

        if (chance == 0 || random.nextDouble() > chance)
            return;

        Direction support = getFacing(state).getOpposite();
        BlockState supportState = level.getBlockState(pos.relative(support));

        if (supportState.is(FlorumSporumBlockTags.SPORE_BLOSSOM_CAN_GROW_ON))
            level.setBlockAndUpdate(pos, SporeBlossomBehaviour.advanceAge(level, pos, state));
    }

    @Override
    public void createBlockStateDefinitionImpl(StateDefinition.Builder<Block, BlockState> builder, Operation<Void> original) {
        super.createBlockStateDefinitionImpl(builder.add(FACING, AGE, OPENNESS), original);
    }

}
