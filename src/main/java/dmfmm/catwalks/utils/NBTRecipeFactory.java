package dmfmm.catwalks.utils;

import com.google.gson.JsonObject;
import dmfmm.catwalks.Catwalks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class NBTRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);
        CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
        primer.width = recipe.getWidth();
        primer.height = recipe.getHeight();
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = recipe.getIngredients();
        String material = JsonUtils.getString(json, "material");
        ItemStack outStack = recipe.getRecipeOutput();
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("material", material);
        outStack.setTagCompound(compound);

        return new ShapedOreRecipe(new ResourceLocation(Catwalks.MODID, "nbt_crafting"), outStack, primer);
    }
}
