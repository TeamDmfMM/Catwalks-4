package dmfmm.catwalks.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICustomModelLocation {

    @SideOnly(Side.CLIENT)
    public void getCustomModelLocation();
}
