package dmfmm.catwalks.block;

import dmfmm.catwalks.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;


public class CableBlock extends GenericBlock {




    public CableBlock() {
        super("cable");

    }

    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if(state instanceof IExtendedBlockState) {
            boolean matchup = world.getBlockState(pos.up()).getBlock() == this;
            boolean matchdown = world.getBlockState(pos.down()).getBlock() == this;

            IExtendedBlockState theState = (IExtendedBlockState) state;

            if(matchup && matchdown) {
                theState = theState.withProperty(STATE, CableState.MIDDLE);
            } else if(matchup) {
                theState = theState.withProperty(STATE, CableState.BOTTOM);
            } else {
                theState = theState.withProperty(STATE, CableState.TOP);
            }

            IBlockState east = world.getBlockState(pos.east());
            IBlockState west = world.getBlockState(pos.west());
            IBlockState north = world.getBlockState(pos.north());
            IBlockState south = world.getBlockState(pos.south());
            Block toCheck = BlockRegistry.CATWALK;

            if(east.getBlock() == toCheck) {
                theState = theState.withProperty(FACING, EnumFacing.EAST);
                theState = theState.withProperty(CONNECTED, true);
            } else if(west.getBlock() == toCheck){
                theState = theState.withProperty(FACING, EnumFacing.WEST);
                theState = theState.withProperty(CONNECTED, true);
            } else if(north.getBlock() == toCheck){
                theState = theState.withProperty(FACING, EnumFacing.NORTH);
                theState = theState.withProperty(CONNECTED, true);
            } else if(south.getBlock() == toCheck){
                theState = theState.withProperty(FACING, EnumFacing.SOUTH);
                theState = theState.withProperty(CONNECTED, true);
            } else {
                theState = theState.withProperty(CONNECTED, false);
            }
            return theState;
        }

        return state;
    }

    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getBoundingBox(state, worldIn, pos));
    }


    public static final AxisAlignedBB CABLE_BOX = new AxisAlignedBB(0.44, 0, 0.44, 0.56, 1, 0.56);
    public static final AxisAlignedBB CLIP_BOX_NORTH_SOUTH = new AxisAlignedBB(0.23, 0, 0, 0.75, 0.13, 0.59);
    public static final AxisAlignedBB CLIP_BOX_EAST_WEST = new AxisAlignedBB(0, 0, 0.23, 0.59, 0.13, 0.75);

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        IBlockState estate = this.getExtendedState(state, source, pos);
        if(estate instanceof IExtendedBlockState){
            IExtendedBlockState estater = (IExtendedBlockState) estate;
            if(estater.getValue(CONNECTED)){
                switch (estater.getValue(FACING)){
                    case NORTH:
                        return CABLE_BOX.union(CLIP_BOX_NORTH_SOUTH);
                    case SOUTH:
                        return CABLE_BOX.union(CLIP_BOX_NORTH_SOUTH).offset(0, 0, 0.4);
                    case EAST:
                        return CABLE_BOX.union(CLIP_BOX_EAST_WEST).offset(0.4, 0, 0);
                    case WEST:
                        return CABLE_BOX.union(CLIP_BOX_EAST_WEST);

                }
            } else {
                return CABLE_BOX;
            }
        }
        return CABLE_BOX;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(CONNECTED).add(FACING).add(STATE).build();
    }


    public static enum CableState implements IStringSerializable {
        TOP, MIDDLE, BOTTOM;

        @Override
        public String getName() {
            return this.toString().toLowerCase();
        }
    }

    public static final IUnlistedProperty<CableState> STATE = new IUnlistedProperty<CableState>() {

        @Override
        public String getName() {
            return "cablestate";
        }

        @Override
        public boolean isValid(CableState value) {
            return true;
        }

        @Override
        public Class<CableState> getType() {
            return CableState.class;
        }

        @Override
        public String valueToString(CableState value) {
            return value.toString().toLowerCase();
        }
    };

    public static final IUnlistedProperty<Boolean> CONNECTED = new IUnlistedProperty<Boolean>() {

        @Override
        public String getName() {
            return "connected";
        }

        @Override
        public boolean isValid(Boolean value) {
            return true;
        }

        @Override
        public Class<Boolean> getType() {
            return Boolean.class;
        }

        @Override
        public String valueToString(Boolean value) {
            return value ? "true" : "false";
        }
    };

    public static final IUnlistedProperty<EnumFacing> FACING = new IUnlistedProperty<EnumFacing>() {
        @Override
        public String getName() {
            return "direction";
        }

        @Override
        public boolean isValid(EnumFacing value) {
            return value != EnumFacing.UP && value != EnumFacing.DOWN;
        }

        @Override
        public Class<EnumFacing> getType() {
            return EnumFacing.class;
        }

        @Override
        public String valueToString(EnumFacing value) {
            return value.toString().toLowerCase();
        }
    };
}
