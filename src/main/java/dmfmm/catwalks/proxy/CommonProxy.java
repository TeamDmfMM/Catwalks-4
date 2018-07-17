package dmfmm.catwalks.proxy;

import dmfmm.catwalks.events.NyanWalkRecipeEvent;
import dmfmm.catwalks.registry.BlockRegistry;
import dmfmm.catwalks.registry.ItemRegistry;
import dmfmm.catwalks.events.ServerChatAddition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void pre(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
        MinecraftForge.EVENT_BUS.register(new BlockRegistry());
        MinecraftForge.EVENT_BUS.register(new ServerChatAddition());
        MinecraftForge.EVENT_BUS.register(new NyanWalkRecipeEvent());
    }

    public void init(FMLInitializationEvent e) {}
    public void post(FMLPostInitializationEvent e) {}
}
