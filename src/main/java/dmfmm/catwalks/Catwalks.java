package dmfmm.catwalks;

import dmfmm.catwalks.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = Catwalks.MODID, version = Catwalks.VERSION, name = Catwalks.MODNAME, acceptedMinecraftVersions = Catwalks.ALLOWED)
public class Catwalks {
    public static final String MODID = "catwalks";
    public static final String MODNAME = "Catwalks";
    public static final String VERSION = "@VERSION@";
    public static final String ALLOWED = "[1.12.2,)";
    public static final String CLIENT = "ClientProxy";
    public static final String SERVER = "CommonProxy";
    public static final String DEPENDENCIES = "required-after:forge@[13.19.1.2195,)";

    public static Side side;

    @Mod.Instance(value = Catwalks.MODID)
 	public static Catwalks instance;

    @SidedProxy(clientSide= Catwalks.CLIENT, serverSide= Catwalks.SERVER)
 	public static CommonProxy proxy;



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        side = e.getSide();
        proxy.pre(e);
        //Testing builds?
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

