package dmf444.catwalks;

import dmf444.catwalks.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Catwalks.MODID, version = Catwalks.VERSION, name = Catwalks.MODNAME, acceptedMinecraftVersions = Catwalks.ALLOWED)
public class Catwalks {
    public static final String MODID = "catwalks";
    public static final String MODNAME = "Catwalks";
    public static final String VERSION = "4.0.0";
    public static final String ALLOWED = "[1.12.2,)";
    public static final String CLIENT = "dmf444.catwalks.proxy.ClientProxy";
    public static final String SERVER = "dmf444.catwalks.proxy.CommonProxy";
    public static final String DEPENDENCIES = "required-after:forge@[13.19.1.2195,)";

    @Mod.Instance(value = Catwalks.MODID)
 	public static Catwalks instance;

    @SidedProxy(clientSide= Catwalks.CLIENT, serverSide= Catwalks.SERVER)
 	public static CommonProxy proxy;



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.pre(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.post(e);
    }
}

