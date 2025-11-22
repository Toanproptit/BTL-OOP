package model;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class BackgroundImageManager {

    private static final String CONFIG_FILE_PATH = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\OOP_BTL\\BTL-OOP\\JavaBTCK-ProGastro\\config.json";

    public static void saveBackgroundImage(String stageId, String imagePath) throws IOException{
        JSONObject config = new JSONObject();
        try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
            config = new JSONObject(new JSONTokener(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.put(stageId, imagePath);
        try (FileWriter file = new FileWriter(CONFIG_FILE_PATH)) {
            file.write(config.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String loadBackgroundImageForStage(String stageId) {
        String imagePath = "";
        File configFile = new File(CONFIG_FILE_PATH);
        if (configFile.exists() && configFile.length() > 0) {
            try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
                JSONObject config = new JSONObject(new JSONTokener(reader));
                if (config.has(stageId)) {
                    imagePath = config.getString(stageId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File config.json không có dữ liệu hợp lệ.");
        }
        return imagePath;
    }
}
