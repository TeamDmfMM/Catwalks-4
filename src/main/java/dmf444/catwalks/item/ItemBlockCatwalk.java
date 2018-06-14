package dmf444.catwalks.item;

import dmf444.catwalks.registry.BlockRegistry;
import dmf444.catwalks.utils.CatwalkMaterial;
import dmf444.catwalks.utils.ICustomModelLocation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCatwalk extends ItemBlock implements ICustomModelLocation {

    public ItemBlockCatwalk() {
        super(BlockRegistry.CATWALK);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getCustomModelLocation() {
        for(CatwalkMaterial material: CatwalkMaterial.values()){
            ModelLoader.registerItemVariants(this, new ModelResourceLocation("catwalks:catwalk_items", "material=" + material.getName().toLowerCase()));
        }
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        String material = "unknown";
        if(stack.hasTagCompound()){
            if(stack.getTagCompound().hasKey("material")){
                material = stack.getTagCompound().getString("material");
            }
        }
        return super.getUnlocalizedName(stack) + "." + material;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for(CatwalkMaterial material: CatwalkMaterial.values()){
                ItemStack stack = new ItemStack(this, 1);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("material", material.getName().toLowerCase());
                stack.setTagCompound(tag);
                items.add(stack);
            }
        }
    }
}
