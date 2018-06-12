package dmf444.catwalks.registry;

import dmf444.catwalks.item.BlowtorchItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.rmi.registry.Registry;
import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ItemRegistry {

    public static ArrayList<Item> ITEMS = new ArrayList<>();

    public static final Item BLOW_TORCH = new BlowtorchItem();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        System.out.print(1);
        ITEMS.forEach(event.getRegistry()::register);
    }
}
