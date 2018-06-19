package dmfmm.catwalks.tileentity;

import net.minecraft.tileentity.TileEntity;

public class LadderTile extends TileEntity implements IConnectTile{

    private boolean hasCage, hasConnection;

    public LadderTile(){
        hasCage = true;
        hasConnection = false;
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
}
