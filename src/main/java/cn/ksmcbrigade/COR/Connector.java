package cn.ksmcbrigade.COR;

import com.google.gson.JsonObject;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod("cor")
public class Connector {

    public static final Logger LOGGER = LogManager.getLogger();
    public static Utils.Manager manager = null;

    public Connector() throws IOException {
        LOGGER.info("Connector mod loading...");
        MinecraftForge.EVENT_BUS.register(this);
        if(!hasAPI()){
            LOGGER.throwing(new RuntimeException("Connector mod requires Vape Manager mod!"));
        }
        init();
        manager = new Utils.Manager("config/cor.json");
        LOGGER.info("Connector mod loaded.");
    }

    public static void init() throws IOException {
        if(!new File("config/vm/mods").exists()){
            new File("config/vm/mods").mkdirs();
        }
        if(!new File("config/vm/mods/connector.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","key.cor.connector");
            object.addProperty("id","cor");
            object.addProperty("main","cn.ksmcbrigade.COR.Manager");
            object.addProperty("function","getKey");
            object.addProperty("function_2","run");
            object.addProperty("gui_main","cn.ksmcbrigade.COR.Manager");
            object.addProperty("gui_function","getScreen");
            Files.write(Paths.get("config/vm/mods/connector.json"),object.toString().getBytes());
        }
    }

    public static boolean hasAPI(){
        for(IModInfo modInfo: ModList.get().getMods()){
            if(modInfo.getModId().equals("vm")) {
                return true;
            }
        }
        return false;
    }
}
