package dmfmm.catwalks.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

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


    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if(compound.hasKey("cage")) {
            hasCage = compound.getBoolean("cage");
            hasConnection = compound.getBoolean("direction");
            facing = EnumFacing.valueOf(compound.getString("facing").toUpperCase());
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        compound.setBoolean("cage", hasCage);
        compound.setBoolean("direction", hasConnection);
        compound.setString("facing", this.facing.getName().toLowerCase());
        return compound;
    }
}
