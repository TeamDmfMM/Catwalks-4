package dmfmm.catwalks.utils;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerChatAddition {


    @SubscribeEvent
    public void addTitle(ServerChatEvent event){
        if(event.getUsername().equals("dmf444") || event.getUsername().equals("mincrmatt12")){
            String newMessage = "§c[Catwalks 4 Dev]§r<" + event.getUsername() + "> " + event.getMessage();
            event.setComponent(new TextComponentString(newMessage));
        }
    }
}
