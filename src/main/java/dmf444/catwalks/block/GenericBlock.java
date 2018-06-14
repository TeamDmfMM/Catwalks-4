package dmf444.catwalks.block;

import dmf444.catwalks.Catwalks;
import dmf444.catwalks.item.GenericItem;
import dmf444.catwalks.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class GenericBlock extends Block {

    public GenericBlock(Material materialIn, String name) {
        super(materialIn);
        this.setRegistryName(new ResourceLocation(Catwalks.MODID, name));
        this.setUnlocalizedName(Catwalks.MODID + "." + name);
        this.setCreativeTab(GenericItem.INSTANCE);
        BlockRegistry.BLOCKS.add(this);
    }

    public GenericBlock(String name){
        this(Material.IRON, name);
    }

}
