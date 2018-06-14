package dmf444.catwalks.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ICustomModelLocation {

    @SideOnly(Side.CLIENT)
    public void getCustomModelLocation();
}
