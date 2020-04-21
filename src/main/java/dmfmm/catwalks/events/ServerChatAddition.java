package dmfmm.catwalks.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerChatAddition {

    private List<String> coolPeople = Arrays.asList("wolfywing", "gearsgoddx", "AcidAngel");


    @SubscribeEvent
    public void addTitle(ServerChatEvent event){
        if(event.getUsername().equals("dmf444") || event.getUsername().equals("mincrmatt12")){
            if(event.getMessage().contains("$s")) {
                String newMessage = "§c[Catwalks 4 Dev]§r<" + event.getUsername() + "> " + event.getMessage().replace("$s", "");
                event.setComponent(new TextComponentString(newMessage));
            }
        } else if(coolPeople.contains(event.getUsername())) {
            if(event.getMessage().contains("$s")) {
                String newMessage = "§9[Catwalks 4 Supporter]§r<" + event.getUsername() + "> " + event.getMessage().replace("$s", "");
                event.setComponent(new TextComponentString(newMessage));
            }
        } else if(event.getUsername().toLowerCase().equals("thecodewarrior")) {
            if(event.getMessage().contains("$s")) {
                String newMessage = "§2[Catwalks 4 Creator]§r<" + event.getUsername() + "> " + event.getMessage().replace("$s", "");
                event.setComponent(new TextComponentString(newMessage));
            }
        }
    }
}
