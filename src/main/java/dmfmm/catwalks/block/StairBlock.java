package dmfmm.catwalks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * File created by mincrmatt12 on 6/22/2018.
 * Originally written for Catwalk.
 * <p>
 * See LICENSE.txt for license information.
 */
public class StairBlock extends GenericBlock {
    public static class State {
        public int up = 0, down = 0;
        public boolean left = false, right = false;

        // facing is stored on blockstate visible

        @Override
        public int hashCode() {
            return new HashCodeBuilder(7, 29).
                    append(up)
                    .append(down)
                    .append(left)
                    .append(right).build();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof State) {
                return obj.hashCode() == this.hashCode();
            }
            return false;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("up", up)
                    .append("down", down)
                    .append("left", left)
                    .append("right", right).build();
        }
    }

    public StairBlock() {
        super(Material.ANVIL, "stair");
        this.setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public static IUnlistedProperty<State> STATE = new IUnlistedProperty<State>() {
        @Override
        public String getName() {
            return "state";
        }

        @Override
        public boolean isValid(State value) {
            return true;
        }

        @Override
        public Class<State> getType() {
            return State.class;
        }

        @Override
        public String valueToString(State value) {
            return value.toString();
        }
    };

    public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState estate = (IExtendedBlockState) state;
            State statey = new State();
            // todo: me
            estate.withProperty(STATE, statey);
            return estate;
        }
        else {
            return state;
        }
    }

    @Override
    public BlockStateContainer getBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING).add(STATE).build();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, entity)), 2);
    }
}
