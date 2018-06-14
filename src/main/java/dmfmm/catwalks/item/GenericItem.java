package dmfmm.catwalks.item;

import dmfmm.catwalks.Catwalks;
import dmfmm.catwalks.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GenericItem extends Item {
    public static CatwalkCreativeTab INSTANCE = new CatwalkCreativeTab();



    public GenericItem(String itemName){
        this.setRegistryName(new ResourceLocation(Catwalks.MODID, itemName));
        this.setUnlocalizedName(Catwalks.MODID + "." + itemName);
        this.setCreativeTab(GenericItem.INSTANCE);
        ItemRegistry.ITEMS.add(this);
    }

    private static class CatwalkCreativeTab extends CreativeTabs{



        public CatwalkCreativeTab() {
            super(Catwalks.MODID);
        }

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.BANNER);
        }
    }

}
