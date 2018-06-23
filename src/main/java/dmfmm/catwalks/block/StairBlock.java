package dmfmm.catwalks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

/**
 * File created by mincrmatt12 on 6/22/2018.
 * Originally written for Catwalk.
 * <p>
 * See LICENSE.txt for license information.
 */
public class StairBlock extends GenericBlock {
    public static class State {
        public int up, down;
        public boolean left, right;
    }

    public StairBlock() {
        super(Material.ANVIL, "stair");
    }


}
