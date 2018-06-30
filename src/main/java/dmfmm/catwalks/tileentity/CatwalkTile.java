package dmfmm.catwalks.tileentity;


import dmfmm.catwalks.utils.CatwalkMaterial;
import dmfmm.catwalks.client.CatwalkState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CatwalkTile extends TileEntity implements IConnectTile{

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
        this.material = CatwalkMaterial.valueOf(materialName.toUpperCase());
    }

    public CatwalkMaterial getMaterial() {
        return material;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setString("material", this.getMaterial().getName());
        for (EnumFacing facing: EnumFacing.values()){
            tag.setBoolean(facing.toString(), this.has(facing));
        }
        return tag;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        String mat = compound.getString("material");
        if(mat != "") {
            this.material = CatwalkMaterial.valueOf(mat.toUpperCase());
        }

        for(EnumFacing facing: EnumFacing.values()) {
            this.updateSide(facing, compound.getBoolean(facing.toString()));
        }
    }


    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        String mat = tag.getString("material");
        if(!mat.equals("")) {
            this.material = CatwalkMaterial.valueOf(mat.toUpperCase());
        }

        for(EnumFacing facing: EnumFacing.values()) {
            this.updateSide(facing, tag.getBoolean(facing.toString()));
        }
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("material", this.getMaterial().getName().toLowerCase());
        for (EnumFacing facing: EnumFacing.values()){
            tag.setBoolean(facing.toString(), this.has(facing));
        }
        return new SPacketUpdateTileEntity(this.getPos(), 1, tag);
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

}
