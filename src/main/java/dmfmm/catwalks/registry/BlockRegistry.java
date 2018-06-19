package dmfmm.catwalks.registry;

import dmfmm.catwalks.Catwalks;
import dmfmm.catwalks.block.CableBlock;
import dmfmm.catwalks.block.CatwalkBlock;
import dmfmm.catwalks.block.LadderBlock;
import dmfmm.catwalks.tileentity.CatwalkTile;
import dmfmm.catwalks.tileentity.LadderTile;
import dmfmm.catwalks.utils.ICustomItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class BlockRegistry {

    public static ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final Block CABLE = new CableBlock();
    public static final Block CATWALK = new CatwalkBlock();
    public static final Block LADDER = new LadderBlock();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(event.getRegistry()::register);

        GameRegistry.registerTileEntity(CatwalkTile.class, new ResourceLocation(Catwalks.MODID, "catwalks"));
        GameRegistry.registerTileEntity(LadderTile.class, new ResourceLocation(Catwalks.MODID, "ladder"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for(Block block: BLOCKS) {
            if(block instanceof ICustomItemBlock) {
                event.getRegistry().register(((ICustomItemBlock) block).getCustomItemBlock().setRegistryName(block.getRegistryName()));
            } else {
                event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }

        }
    }
}
