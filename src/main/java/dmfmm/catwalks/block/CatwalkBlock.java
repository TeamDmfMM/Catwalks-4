package dmfmm.catwalks.block;

import dmfmm.catwalks.item.ItemBlockCatwalk;
import dmfmm.catwalks.tileentity.CatwalkStateCalculator;
import dmfmm.catwalks.tileentity.IConnectTile;
import dmfmm.catwalks.utils.CatwalkMaterial;
import dmfmm.catwalks.Catwalks;
import dmfmm.catwalks.client.CatwalkState;
import dmfmm.catwalks.registry.ItemRegistry;
import dmfmm.catwalks.tileentity.CatwalkTile;
import dmfmm.catwalks.utils.ICustomItemBlock;
import dmfmm.catwalks.utils.Pair;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class CatwalkBlock extends GenericBlock implements ITileEntityProvider, ICustomItemBlock {

    //public static UnlistedArbitraryProperty CATWALK_STATE = new UnlistedArbitraryProperty("state", CatwalkState.class);

    public static final IUnlistedProperty<CatwalkState> CATWALK_STATE  = new IUnlistedProperty<CatwalkState>() {

        @Override
        public String getName() {
            return "cablestate";
        }

        @Override
        public boolean isValid(CatwalkState value) {
            return true;
        }

        @Override
        public Class<CatwalkState> getType() {
            return CatwalkState.class;
        }

        @Override
        public String valueToString(CatwalkState value) {
            return value.toString().toLowerCase();
        }
    };
    public static PropertyEnum<CatwalkMaterial> MATERIAL = PropertyEnum.create("material", CatwalkMaterial.class);

    private static Map<EnumFacing, Pair<AxisAlignedBB, AxisAlignedBB>> boundingBoxes = new HashMap<>();
    static {
        boundingBoxes.put(EnumFacing.DOWN, new Pair<>(aabb( 0, 0,  0, 16,  0, 16), aabb( 4, 0,  4, 12, 0, 12)));
        boundingBoxes.put(EnumFacing.NORTH, new Pair<>( aabb( 0, 0,  0, 16, 16,  0), aabb( 0, 0,  0, 16, 8,  0)));
        boundingBoxes.put(EnumFacing.SOUTH, new Pair<>(aabb( 0, 0, 16, 16, 16, 16), aabb( 0, 0, 16, 16, 8, 16)));
        boundingBoxes.put(EnumFacing.WEST, new Pair<>(aabb( 0, 0,  0,  0, 16, 16), aabb( 0, 0,  0,  0, 8, 16)));
        boundingBoxes.put(EnumFacing.EAST, new Pair<>(aabb(16, 0,  0, 16, 16, 16), aabb(16, 0,  0, 16, 8, 16)));
    }

    private static double p(int pixels){
        return pixels / 16.0;
    }
    private static AxisAlignedBB aabb(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new AxisAlignedBB(p(minX), p(minY), p(minZ), p(maxX), p(maxY), p(maxZ));
    }

    public CatwalkBlock() {
        super("catwalk");
        this.setHardness(1.0f);
        this.setResistance(5f);
        this.setSoundType(SoundType.METAL);
    }


    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(player.getHeldItem(hand).getItem() != ItemRegistry.BLOW_TORCH)
            return false;
        TileEntity tileEntity = world.getTileEntity(pos);
        if(!(tileEntity instanceof CatwalkTile))
            return false;
        ((CatwalkTile) tileEntity).updateSide(facing, !((CatwalkTile) tileEntity).getSideState(facing));
        world.notifyBlockUpdate(pos, state, state, 2);
        return true;
    }

    @SuppressWarnings("deprecation")
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
    {
        CatwalkTile tile = worldIn.getTileEntity(pos) instanceof CatwalkTile ? (CatwalkTile) worldIn.getTileEntity(pos) : null;
        Map<Pair<EnumFacing, AxisAlignedBB>, RayTraceResult> map = new HashMap<>();
        for (Map.Entry<EnumFacing, Pair<AxisAlignedBB, AxisAlignedBB>> it: boundingBoxes.entrySet()) {
            boolean has = tile.has(it.getKey());
            AxisAlignedBB bb = has ? it.getValue().getKey() : it.getValue().getValue();
            map.put(new Pair<>(it.getKey(), bb), rayTrace(pos, start, end, bb));
        }
        Optional<Map.Entry<Pair<EnumFacing, AxisAlignedBB>, RayTraceResult>> min = map.entrySet().stream().filter(x -> x.getValue() != null).min(Comparator.comparingDouble(p -> p.getValue().hitVec.lengthSquared()));
        if(min.isPresent()) {
            Map.Entry<Pair<EnumFacing, AxisAlignedBB>, RayTraceResult> pairRayTraceResultEntry = min.get();
            RayTraceResult traceResult = new RayTraceResult(pairRayTraceResultEntry.getValue().typeOfHit, pairRayTraceResultEntry.getValue().hitVec, pairRayTraceResultEntry.getKey().getKey(), pairRayTraceResultEntry.getValue().getBlockPos());
            Pair<EnumFacing, AxisAlignedBB> savedPair = pairRayTraceResultEntry.getKey();
            if(Catwalks.side == Side.CLIENT) {
                this.updateBounds(savedPair.getValue());
            }
            return traceResult;
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof CatwalkTile) {
            CatwalkTile tile = (CatwalkTile) tileEntity;
            if (stack != ItemStack.EMPTY && stack.hasTagCompound() && stack.getTagCompound().hasKey("material")) {
                String materialName = stack.getTagCompound().getString("material");
                try {
                    CatwalkMaterial mat = CatwalkMaterial.valueOf(materialName.toUpperCase());
                    tile.updateMaterial(mat.getName());
                } catch (IllegalArgumentException e) {
                    System.out.println("ass");
                }
            }

            for(EnumFacing facing: EnumFacing.HORIZONTALS){
                TileEntity other = world.getTileEntity(pos.offset(facing));
                if(other instanceof CatwalkTile) {
                    ((CatwalkTile) other).updateSide(facing.getOpposite(), false);
                    tile.updateSide(facing, false);
                }
                if(other instanceof IConnectTile) {
                    tile.updateSide(facing, false);
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockDestroyedByPlayer(world, pos, state);

        for(EnumFacing facing: EnumFacing.HORIZONTALS) {
            TileEntity other = world.getTileEntity(pos.offset(facing));
            if(other instanceof CatwalkTile) {
                ((CatwalkTile) other).updateSide(facing.getOpposite(), true);
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        super.breakBlock(world, pos, state);
        CatwalkStateCalculator.removeFromCache(pos);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ItemStack stack = new ItemStack(this, 1);
        CatwalkMaterial mat = state.getValue(MATERIAL);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("material", mat.getName().toLowerCase());
        stack.setTagCompound(tag);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(CATWALK_STATE).add(MATERIAL).build();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState s = super.getActualState(state, worldIn, pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof CatwalkTile){
            s = s.withProperty(MATERIAL, ((CatwalkTile) tileEntity).getMaterial());
        }
        return s;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        IExtendedBlockState estate = (IExtendedBlockState) super.getExtendedState(state, world, pos);
        if(tileEntity instanceof CatwalkTile) {
            estate = estate.withProperty(CATWALK_STATE, ((CatwalkTile) tileEntity).getCatwalkState());
        }
        return estate;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof CatwalkTile){
            for(EnumFacing facing :boundingBoxes.keySet()){
                boolean exists = ((CatwalkTile) tileEntity).getSideState(facing);
                if(exists) {
                    AxisAlignedBB bb = boundingBoxes.get(facing).getKey().offset(pos);
                    if(bb.intersects(entityBox)){
                        collidingBoxes.add(bb);
                    }
                }
            }
        }
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CatwalkTile();
    }

    @Override
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    private AxisAlignedBB bounds = FULL_BLOCK_AABB;
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    private void updateBounds(AxisAlignedBB bb){
        bounds = bb;
    }

    @Override
    public ItemBlock getCustomItemBlock() {
        return new ItemBlockCatwalk();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CatwalkTile) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("material", ((CatwalkTile) tileEntity).getMaterial().getName());
            ItemStack is = new ItemStack(this, 1);
            is.setTagCompound(nbt);
            return is;
        }
        else {
            return super.getPickBlock(state, target, world, pos, player);
        }
    }
}
