package com.axialeaa.florumsporum.block.property;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

//? if <=1.21.1
/*import net.minecraft.state.property.DirectionProperty;*/

public class SporeBlossomProperties {

    public static final /*$ direction_property >>*/ EnumProperty<Direction> FACING = Properties.FACING;
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);
    public static final IntProperty AGE = Properties.AGE_3;

    public static final int MAX_AGE = 3;
    public static final int GROWTH_STAGE_COUNT = MAX_AGE + 1;

}
