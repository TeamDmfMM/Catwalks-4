package dmfmm.catwalks.block;


import dmfmm.catwalks.registry.BlockRegistry;
import dmfmm.catwalks.registry.ItemRegistry;
import dmfmm.catwalks.tileentity.IConnectTile;
import dmfmm.catwalks.tileentity.LadderTile;
import dmfmm.catwalks.utils.LadderBlockHitboxes;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;

public class LadderBlock extends GenericBlock implements ITileEntityProvider{


    public LadderBlock() {
        super("ladder");
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(player.getHeldItem(hand).getItem() != ItemRegistry.BLOW_TORCH)
            return false;
        TileEntity tileEntity = world.getTileEntity(pos);
        if(!(tileEntity instanceof LadderTile))
            return false;
        EnumFacing tileFacing = ((LadderTile) tileEntity).getFacing();
        if(facing == tileFacing && world.getTileEntity(pos.offset(facing)) instanceof IConnectTile){
            ((LadderTile) tileEntity).setHasConnection(!((LadderTile) tileEntity).doesHaveConnection());
        }else if(facing == tileFacing.getOpposite()){
            ((LadderTile) tileEntity).setHasCage(!((LadderTile) tileEntity).doesHaveCage());
        }
        world.notifyBlockUpdate(pos, state, state, 2);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof LadderTile){
            EnumFacing f = EnumFacing.getDirectionFromEntityLiving(pos, entity).getOpposite();
            if(f == EnumFacing.UP || f == EnumFacing.DOWN){
                f = EnumFacing.NORTH;
            }
            ((LadderTile) te).setFacing(f);
        }
    }

    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if(state instanceof IExtendedBlockState) {
            boolean matchup = world.getBlockState(pos.up()).getBlock() == this;
            boolean matchdown = world.getBlockState(pos.down()).getBlock() == this;
            TileEntity te = world.getTileEntity(pos);

            IExtendedBlockState theState = (IExtendedBlockState) state;

            if(te instanceof LadderTile){
                theState = theState.withProperty(HAS_CAGE, ((LadderTile) te).doesHaveCage());
                theState = theState.withProperty(FACING, ((LadderTile) te).getFacing());
            } else {
                theState = theState.withProperty(HAS_CAGE, true);
                theState = theState.withProperty(FACING, EnumFacing.NORTH);
            }


            if(matchup && matchdown) {
                theState = theState.withProperty(STATE, LadderState.MIDDLE);
            } else if(matchup) {
                theState = theState.withProperty(STATE, LadderState.BOTTOM);
                TileEntity tes = world.getTileEntity(pos.offset(EnumFacing.NORTH.getOpposite()));
                if(tes instanceof IConnectTile && !(tes instanceof LadderTile)){
                    theState = theState.withProperty(CONNECTED, true);
                } else {
                    theState = theState.withProperty(CONNECTED, false);
                }
            } else {
                theState = theState.withProperty(STATE, LadderState.TOP);
                if(te instanceof LadderTile){
                    theState = theState.withProperty(CONNECTED, ((LadderTile) te).doesHaveConnection());
                }else {
                    theState = theState.withProperty(CONNECTED, false);
                }
            }

            return theState;
        }

        return state;
    }

    @Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        if(entity == null){
            return false;
        }
        IExtendedBlockState estate = (IExtendedBlockState) this.getExtendedState(state, world, pos);
        Double[] ladder = LadderBlockHitboxes.getLadderBox(estate.getValue(FACING));
        AxisAlignedBB bb = new AxisAlignedBB(pos.getX() + ladder[0], pos.getY() + ladder[1], pos.getZ() + ladder[2], pos.getX() + ladder[3], pos.getY() + ladder[4], pos.getZ() + ladder[5]);
        AxisAlignedBB ba = entity.getEntityBoundingBox();
        return bb.minX <= ba.maxX && bb.maxX >= ba.minX && bb.minY < ba.maxY && bb.maxY > ba.minY && bb.minZ <= ba.maxZ && bb.maxZ >= ba.minZ;

    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        IExtendedBlockState estate = (IExtendedBlockState) this.getExtendedState(state, world, pos);
        Double[][] hit = LadderBlockHitboxes.getBoxAdditions(estate.getValue(FACING));
        AxisAlignedBB bb = new AxisAlignedBB(pos.getX() + hit[1][0], pos.getY() + hit[1][1], pos.getZ() + hit[1][2], pos.getX() + hit[1][3], pos.getY() + hit[1][4], pos.getZ() + hit[1][5]);

        //side panels
        if(estate.getValue(STATE) == LadderState.BOTTOM ||estate.getValue(STATE) == LadderState.TOP || (estate.getValue(STATE) == LadderState.MIDDLE && estate.getUnlistedNames().contains(HAS_CAGE) && estate.getValue(HAS_CAGE))) {
            if (bb.intersects(entityBox)) {
                collidingBoxes.add(bb);
            }
            bb = new AxisAlignedBB(pos.getX() + hit[2][0], pos.getY() + hit[2][1], pos.getZ() + hit[2][2], pos.getX() + hit[2][3], pos.getY() + hit[2][4], pos.getZ() + hit[2][5]);
            if (bb.intersects(entityBox)) {
                collidingBoxes.add(bb);
            }
        }
        //ladderblock
        if(estate.getValue(STATE) == LadderState.BOTTOM || estate.getValue(STATE) == LadderState.MIDDLE || (estate.getValue(STATE) == LadderState.TOP && estate.getUnlistedNames().contains(STATE) && !estate.getValue(CONNECTED))){
            bb = new AxisAlignedBB(pos.getX() + hit[0][0], pos.getY() + hit[0][1], pos.getZ() + hit[0][2], pos.getX() + hit[0][3], pos.getY() + hit[0][4], pos.getZ() + hit[0][5]);
            if (bb.intersects(entityBox)) {
                collidingBoxes.add(bb);
            }
        }

        //cage box
        if(estate.getValue(STATE) == LadderState.TOP || (estate.getValue(STATE) == LadderState.MIDDLE && estate.getUnlistedNames().contains(HAS_CAGE) && estate.getValue(HAS_CAGE))){
            bb = new AxisAlignedBB(pos.getX() + hit[3][0], pos.getY() + hit[3][1], pos.getZ() + hit[3][2], pos.getX() + hit[3][3], pos.getY() + hit[3][4], pos.getZ() + hit[3][5]);
            if (bb.intersects(entityBox)) {
                collidingBoxes.add(bb);
            }
        }
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
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(CONNECTED).add(HAS_CAGE).add(STATE).add(FACING).build();
    }

    public static final IUnlistedProperty<Boolean> HAS_CAGE = new IUnlistedProperty<Boolean>() {

        @Override
        public String getName() {
            return "caged";
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

    public static final IUnlistedProperty<Boolean> CONNECTED = new IUnlistedProperty<Boolean>() {

        @Override
        public String getName() {
            return "connectible";
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

    public static final IUnlistedProperty<LadderState> STATE = new IUnlistedProperty<LadderState>() {

        @Override
        public String getName() {
            return "ladderstate";
        }

        @Override
        public boolean isValid(LadderState value) {
            return true;
        }

        @Override
        public Class<LadderState> getType() {
            return LadderState.class;
        }

        @Override
        public String valueToString(LadderState value) {
            return value.toString().toLowerCase();
        }
    };

    public static final IUnlistedProperty<EnumFacing> FACING = new IUnlistedProperty<EnumFacing>() {
        @Override
        public String getName() {
            return "facing";
        }

        @Override
        public boolean isValid(EnumFacing value) {
            return true;
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new LadderTile();
    }


    public static enum LadderState implements IStringSerializable {
        TOP, MIDDLE, BOTTOM;

        @Override
        public String getName() {
            return this.toString().toLowerCase();
        }
    }
}
