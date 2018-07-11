package dmfmm.catwalks.block;

import dmfmm.catwalks.Catwalks;
import dmfmm.catwalks.item.GenericItem;
import dmfmm.catwalks.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

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

}
