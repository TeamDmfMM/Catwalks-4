package dmf444.catwalks.tileentity;


import dmf444.catwalks.client.CatwalkModel;
import dmf444.catwalks.client.CatwalkState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;


public class CatwalkStateCalculator {

    static HashMap<Long, CatwalkTile> tileCache = new HashMap<>();
    World world;
    BlockPos pos;

    public CatwalkStateCalculator(World world, BlockPos pos){
        this.world = world;
        this.pos = pos;
    }

    @Nullable
    public CatwalkTile getCached(BlockPos pos) {
        long l = pos.toLong();
        if(tileCache.containsKey(l)){
            return tileCache.get(l);
        }
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof CatwalkTile) {
            tileCache.put(l, (CatwalkTile) tileEntity);
            return (CatwalkTile) tileEntity;
        }
        return null;
    }

    public boolean exists(BlockPos pos) {
        return getCached(pos) != null;
    }

    public boolean has(EnumFacing side) {
        return has(side, this.pos);
    }

    public boolean has(EnumFacing side, BlockPos pos) {
        return getCached(pos).has(side);
    }

    public CatwalkState calculate() {
        return new CatwalkState(
                getRailState(EnumFacing.WEST, EnumFacing.NORTH),
                getRailState(EnumFacing.EAST, EnumFacing.NORTH),
                getRailState(EnumFacing.WEST, EnumFacing.SOUTH),
                getRailState(EnumFacing.EAST, EnumFacing.SOUTH),

                !has(EnumFacing.DOWN) ? null : getFloorState(EnumFacing.WEST, EnumFacing.NORTH),
                !has(EnumFacing.DOWN) ? null : getFloorState(EnumFacing.EAST, EnumFacing.NORTH),
                !has(EnumFacing.DOWN) ? null : getFloorState(EnumFacing.WEST, EnumFacing.SOUTH),
                !has(EnumFacing.DOWN) ? null : getFloorState(EnumFacing.EAST, EnumFacing.SOUTH)
        );
    }

    public CatwalkModel.RailSection getRailState(EnumFacing xAxis, EnumFacing zAxis){
        BlockPos posX = pos.offset(xAxis);
        BlockPos posZ = pos.offset(zAxis);
        BlockPos corner = pos.offset(xAxis).offset(zAxis);

        if(has(xAxis) && has(zAxis))
            return CatwalkModel.RailSection.OUTER;

        if(has(xAxis)) {
            if(exists(posZ) && !has(zAxis.getOpposite(), posZ)) {
                if(has(xAxis, posZ)) {
                    return CatwalkModel.RailSection.Z_EDGE;
                } else if(exists(corner) && has(zAxis.getOpposite(), corner) && !has(xAxis.getOpposite(), corner)){
                    return CatwalkModel.RailSection.Z_EDGE;
                }
            }
            return CatwalkModel.RailSection.Z_END;
        }

        if(has(zAxis)) {
            if(exists(posX) && !has(xAxis.getOpposite().getOpposite(), posX)) {
                if (has(zAxis, posX)) {
                    return CatwalkModel.RailSection.X_EDGE;
                } else if(exists(corner) && has(xAxis.getOpposite(), corner) && !has(zAxis.getOpposite(), corner)) {
                    return CatwalkModel.RailSection.X_EDGE;
                }
            }
            return CatwalkModel.RailSection.X_END;
        }

        if(!has(xAxis) && !has(zAxis)) {
            if(exists(posX) && !has(xAxis.getOpposite(), posX) && has(zAxis, posX) && exists(posZ) && !has(zAxis.getOpposite(), posZ) && has(xAxis, posZ)) {
                return CatwalkModel.RailSection.INNER;
            }
        }
        return CatwalkModel.RailSection.MIDDLE;

    }

    public CatwalkModel.FloorSection getFloorState(EnumFacing xAxis, EnumFacing zAxis) {
        BlockPos posX = pos.offset(xAxis);
        BlockPos posZ = pos.offset(zAxis);
        BlockPos corner = pos.offset(xAxis).offset(zAxis);

        if(has(xAxis) && has(zAxis))
            return CatwalkModel.FloorSection.OUTER;

        if(!has(xAxis) && !has(zAxis) && exists(posX) && has(EnumFacing.DOWN, posX) && !has(xAxis.getOpposite(), posX) && exists(posZ) && has(EnumFacing.DOWN, posZ) && !has(zAxis.getOpposite(), posZ)
                ) {
            if(exists(corner) && has(EnumFacing.DOWN, corner) && !has(xAxis.getOpposite(), corner) && !has(zAxis.getOpposite(), corner))
                return CatwalkModel.FloorSection.MIDDLE;
            return CatwalkModel.FloorSection.INNER;
        }

        if(!has(zAxis)) {
            if(exists(posZ) && has(EnumFacing.DOWN, posZ) && !has(zAxis.getOpposite(), posZ))
                return CatwalkModel.FloorSection.Z_EDGE;
        }
        if(!has(xAxis)) {
            if(exists(posX) && has(EnumFacing.DOWN, posX) && !has(xAxis.getOpposite(), posX))
                return CatwalkModel.FloorSection.X_EDGE;
        }

        return CatwalkModel.FloorSection.OUTER;

    }


}
