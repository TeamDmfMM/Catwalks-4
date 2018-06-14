package dmf444.catwalks.proxy;

import dmf444.catwalks.client.CableModelLoader;
import dmf444.catwalks.registry.ItemRegistry;
import dmf444.catwalks.utils.ICustomModelLocation;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy{


    public void pre(FMLPreInitializationEvent e) {
        super.pre(e);

        MinecraftForge.EVENT_BUS.register(this);
        ModelLoaderRegistry.registerLoader(new CableModelLoader());
    }

    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    public void post(FMLPostInitializationEvent e) {
        super.post(e);
    }


    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        for(Item item : ItemRegistry.ITEMS) {
            if(item instanceof ICustomModelLocation) {
                ((ICustomModelLocation) item).getCustomModelLocation();
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
    }


}
