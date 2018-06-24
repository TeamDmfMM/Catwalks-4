package dmfmm.catwalks.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class LadderTile extends TileEntity implements IConnectTile{

    private boolean hasCage, hasConnection;
    private EnumFacing facing;

    public LadderTile(){
        hasCage = true;
        hasConnection = false;
        facing = EnumFacing.NORTH;
    }

    public boolean doesHaveCage() {
        return hasCage;
    }

    public void setHasCage(boolean hasCage) {
        this.hasCage = hasCage;
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }

    public boolean doesHaveConnection() {
        return hasConnection;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }
}
