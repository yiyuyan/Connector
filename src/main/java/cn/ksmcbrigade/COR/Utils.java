package cn.ksmcbrigade.COR;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Utils {

    public static String readFile(String filePath) throws IOException {
        String path = filePath;
        if(System.getProperty("os.name").contains("Windows")){
            path = filePath.replace("/","\\");
        }
        File file = new File(path);
        long fileSize = file.length();
        FileInputStream fis = new FileInputStream(file);
        byte[] fileData = new byte[(int) fileSize];
        fis.read(fileData);
        fis.close();
        return new String(fileData, StandardCharsets.UTF_8);
    }

    public static int getSleep(int sleep,int skew){
        SecureRandom random = new SecureRandom();
        if(random.nextBoolean()){
            return new SecureRandom().nextInt(sleep-skew,sleep);
        }
        else{
            return new SecureRandom().nextInt(sleep,sleep+skew);
        }
    }

    public static void Click(boolean LEFT) {
        WinDef.POINT currentPos = new WinDef.POINT();
        MouseClick.INSTANCE.GetCursorPos(currentPos);
        int x = currentPos.x;
        int y = currentPos.y;
        if(LEFT){
            MouseClick.INSTANCE.mouse_event(2, x, y, 0, 0);
            MouseClick.INSTANCE.mouse_event(4, x, y, 0, 0);
        }
        else{
            MouseClick.INSTANCE.mouse_event(8, x, y, 0, 0);
            MouseClick.INSTANCE.mouse_event(10, x, y, 0, 0);
        }
    }

    public static void BlockingDelay(int delayInMilliseconds) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < delayInMilliseconds) {
            // no codes.
        }
    }

    static class Manager {
        private final String path;
        private boolean IN_SERVER;
        private boolean LEFT;
        private int SLEEP;
        private int SLEEP_SKEW;
        private int SLEEPING = 0;
        public Manager(String path) throws IOException {
            this.path = path;
            if(!new File(path).exists()){
                JsonObject json = new JsonObject();
                json.addProperty("IN_SERVER", false);
                json.addProperty("LEFT", true);
                json.addProperty("SLEEP", 10);
                json.addProperty("SLEEP_SKEW", 10);
                File file = new File(path);
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(json.toString());
                writer.flush();
                writer.close();
            }
            JsonObject json = JsonParser.parseString(readFile(path)).getAsJsonObject();
            this.IN_SERVER = json.get("IN_SERVER").getAsBoolean();
            this.LEFT = json.get("LEFT").getAsBoolean();
            this.SLEEP = json.get("SLEEP").getAsInt();
            this.SLEEP_SKEW = json.get("SLEEP_SKEW").getAsInt();
        }

        @Override
        public String toString() {
            return "Manager{" +
                    "path='" + path + '\'' +
                    ", IN_SERVER=" + IN_SERVER +
                    ", LEFT=" + LEFT +
                    ", SLEEP=" + SLEEP +
                    ", SLEEP_SKEW=" + SLEEP_SKEW +
                    '}';
        }

        public int getSLEEP() {
            return SLEEP;
        }

        public int getSLEEP_SKEW() {
            return SLEEP_SKEW;
        }

        public boolean isIN_SERVER() {
            return IN_SERVER;
        }

        public boolean isLEFT() {
            return LEFT;
        }

        public void setSLEEP(int Sleep) {
            this.SLEEP = Sleep;
        }

        public void setSleepSkew(int Skew) {
            this.SLEEP_SKEW = Skew;
        }

        public void setIN_SERVER(boolean IN_SERVER) {
            this.IN_SERVER = IN_SERVER;
        }

        public void setLEFT(boolean LEFT) {
            this.LEFT = LEFT;
        }

        public int getSleep(){
            return Utils.getSleep(this.SLEEP,this.SLEEP_SKEW);
        }

        public boolean click() {
            if(SLEEPING==0){
                if(!Minecraft.getInstance().hasSingleplayerServer() && !this.IN_SERVER){
                    return false;
                }
                Click(this.LEFT);
                SLEEPING = getSLEEP();
                return true;
            }
            else{
                SLEEPING--;
                return false;
            }
        }

        public void save() throws IOException {
            JsonObject json = new JsonObject();
            json.addProperty("IN_SERVER", this.IN_SERVER);
            json.addProperty("LEFT", this.LEFT);
            json.addProperty("SLEEP", this.SLEEP);
            json.addProperty("SLEEP_SKEW", this.SLEEP);
            FileWriter writer = new FileWriter(new File(this.path));
            writer.write(json.toString());
            writer.flush();
            writer.close();
        }
    }

    public interface MouseClick extends User32 {
        MouseClick INSTANCE = Native.load("user32", Utils.MouseClick.class);

        boolean GetCursorPos(WinDef.POINT lpPoint);
        void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);
    }
}
