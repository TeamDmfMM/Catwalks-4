package dmf444.catwalks.block;

import dmf444.catwalks.Catwalks;
import dmf444.catwalks.item.GenericItem;
import dmf444.catwalks.registry.BlockRegistry;
import dmf444.catwalks.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;


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

    @Override
    public boolean isOpaqueCube(IBlockState state) {
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
