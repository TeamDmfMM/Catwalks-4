package dmfmm.catwalks.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class NBTBuilder {

    private NBTTagCompound tag;

    public NBTBuilder(){
        tag = new NBTTagCompound();
    }

    public NBTBuilder add(String key, String value){
        tag.setString(key, value);
        return this;
    }

    public NBTBuilder add(String key, double value){
        tag.setDouble(key, value);
        return this;
    }

    public NBTBuilder add(String key, float value){
        tag.setFloat(key, value);
        return this;
    }

    public NBTBuilder add(String key, NBTBase value){
        tag.setTag(key, value);
        return this;
    }

    public NBTBuilder add(String key, boolean value){
        tag.setBoolean(key, value);
        return this;
    }

    public NBTBuilder add(String key, ItemStack value){
        tag.setTag(key, value.writeToNBT(new NBTTagCompound()));
        return this;
    }

    public NBTBuilder add(String key, EnumFacing value){
        tag.setString(key, value.toString().toLowerCase());
        return this;
    }

    public NBTBuilder add(String key, BlockPos value){
        tag.setLong(key, value.toLong());
        return this;
    }

    public NBTTagCompound build() {
        return tag;
    }
}
