package dmfmm.catwalks.utils;

import javafx.util.Pair;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LadderBlockHitboxes {

    private  static Map<EnumFacing, Double[][]> FACEMAP = new HashMap<>();

    static {
        //NORTH
        Double[][] northBoxes = {
                {0.1, 0.0, 0.01, 0.9, 1.0, 0.13}, //LADDER AABB
                {0.0, 0d, 0.01, 0.06, 1d, 0.64}, //Side panel 1
                {0.94, 0.0, 0.01, 1.0, 1.0, 0.64}, //Side panel 2
                {0.1, 0.0, 0.85, 0.9, 1d, 0.90} //Cage Hitbox
        };
        FACEMAP.put(EnumFacing.NORTH, northBoxes);
        //South
        Double[][] southBoxes = {
                {0.1, 0.0, 0.87, 0.9, 1.0, 0.99}, //LADDER BOX
                {0.0, 0d, 0.36, 0.06, 1d, 0.99}, //SIDE PANEL 1
                {0.94, 0.0, 0.36, 1.0, 1.0, 0.99}, //SIDE PANEL 2
                {0.1, 0.0, 0.1, 0.9, 1d, 0.15} //CAGE BOX
        };
        FACEMAP.put(EnumFacing.SOUTH, southBoxes);
        //West
        Double[][] westBoxes = {
                {0.01, 0.0, 0.1, 0.13, 1.0, 0.9}, //LADDER BOX
                {0.01, 0d, 0.0, 0.64, 1d, 0.06}, //Side panel 1
                {0.01, 0.0, 0.94, 0.64, 1.0, 1.0}, //Side panel 2
                {0.85, 0.0, 0.1, 0.90, 1d, 0.9} //Cage Hitbox
        };
        FACEMAP.put(EnumFacing.WEST, westBoxes);
        //East
        Double[][] eastBoxes = {
                {0.87, 0.0, 0.1, 0.99, 1.0, 0.9}, //LADDER BOX
                {0.36, 0d, 0.0, 0.99, 1d, 0.06}, //SIDE PANEL 1
                {0.36, 0.0, 0.94, 0.99, 1.0, 1.0}, //SIDE PANEL 2
                {0.1, 0.0, 0.1, 0.15, 1d, 0.9} //CAGE BOX
        };
        FACEMAP.put(EnumFacing.EAST, eastBoxes);
    }

    public static Double[][] getBoxAdditions(EnumFacing facing){
        if(facing == EnumFacing.DOWN || facing == EnumFacing.UP){
            return FACEMAP.get(EnumFacing.NORTH);
        }
        return FACEMAP.get(facing);
    }

    public static Double[] getLadderBox(EnumFacing facing){
        if(facing == EnumFacing.DOWN || facing == EnumFacing.UP){
            return FACEMAP.get(EnumFacing.NORTH)[0];
        }
        return FACEMAP.get(facing)[0];
    }

}
