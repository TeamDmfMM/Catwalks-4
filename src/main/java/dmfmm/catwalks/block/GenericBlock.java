package dmfmm.catwalks.block;

import dmfmm.catwalks.Catwalks;
import dmfmm.catwalks.item.GenericItem;
import dmfmm.catwalks.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class GenericBlock extends Block {

    public GenericBlock(Material materialIn, String name) {
        super(materialIn);
        this.setRegistryName(new ResourceLocation(Catwalks.MODID, name));
        this.setUnlocalizedName(Catwalks.MODID + "." + name);
        this.setCreativeTab(GenericItem.INSTANCE);
        this.setHardness(1.0f);
        this.setResistance(5f);
        BlockRegistry.BLOCKS.add(this);
    }

    public GenericBlock(String name){
        this(Material.IRON, name);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.UP) {
            return BlockFaceShape.UNDEFINED;
        }

        return super.getBlockFaceShape(worldIn, state, pos, face);
    }


}
