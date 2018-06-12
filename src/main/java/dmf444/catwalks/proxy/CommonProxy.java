package dmf444.catwalks.proxy;

import dmf444.catwalks.registry.BlockRegistry;
import dmf444.catwalks.registry.ItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void pre(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
        MinecraftForge.EVENT_BUS.register(new BlockRegistry());
    }

    public void init(FMLInitializationEvent e) {}
    public void post(FMLPostInitializationEvent e) {}
}
