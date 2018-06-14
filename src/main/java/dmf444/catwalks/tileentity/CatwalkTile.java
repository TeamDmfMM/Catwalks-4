package dmf444.catwalks.tileentity;


import dmf444.catwalks.utils.CatwalkMaterial;
import dmf444.catwalks.client.CatwalkState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class CatwalkTile extends TileEntity {

    Map<EnumFacing, Boolean> sides = new HashMap<>();
    CatwalkMaterial material = CatwalkMaterial.CLASSIC;

    public CatwalkTile(){
        for (EnumFacing facing: EnumFacing.values()) {
            sides.put(facing, true);
        }
    }

    public void updateSide(EnumFacing side, boolean state){
        sides.replace(side, state);
    }

    public boolean getSideState(EnumFacing facing) {
        return sides.get(facing);
    }

    public boolean has(EnumFacing side) {
        return sides.get(side);
    }

    //@SideOnly(Side.CLIENT)
    public CatwalkState getCatwalkState(){
        return new CatwalkStateCalculator(this.getWorld(), this.getPos()).calculate();
    }

    public void updateMaterial(String materialName){
        this.material = CatwalkMaterial.valueOf(materialName);
    }

    public CatwalkMaterial getMaterial() {
        return material;
    }

}
