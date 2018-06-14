package dmf444.catwalks.registry;

import dmf444.catwalks.block.CableBlock;
import dmf444.catwalks.block.CatwalkBlock;
import dmf444.catwalks.utils.ICustomItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class BlockRegistry {

    public static ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final Block CABLE = new CableBlock();
    public static final Block CATWALK = new CatwalkBlock();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(event.getRegistry()::register);
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
