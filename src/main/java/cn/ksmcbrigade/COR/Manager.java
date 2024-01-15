package cn.ksmcbrigade.COR;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import static cn.ksmcbrigade.COR.Utils.BlockingDelay;

public class Manager {

    public static final KeyMapping CONNECTOR_KEY = new KeyMapping("key.cor.connector", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F10,"key.cor.connector");

    public static KeyMapping getKey(){
        return CONNECTOR_KEY;
    }

    public static Screen getScreen(){
        return new SettingGUI();
    }

    public static void run(Player player){
        if(Minecraft.getInstance().hasSingleplayerServer()){
            Connector.manager.click();
        }
        else{
            if(Connector.manager.isIN_SERVER()){
                Connector.manager.click();
                BlockingDelay(1);
            }
            else{
                player.sendMessage(new TranslatableComponent("chat.cor.waring"),player.getUUID());
            }
        }
    }
}
