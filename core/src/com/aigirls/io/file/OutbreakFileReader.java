package com.aigirls.io.file;

import com.badlogic.gdx.math.Vector2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.FileManager;
import com.aigirls.model.MagicOutbreakModel;
import com.badlogic.gdx.utils.Array;

public class OutbreakFileReader {
    public static char BALL_CHARACTER = '*';

    public static MagicOutbreakModel getMagicOutbreakModel(String fileName) {
        MagicOutbreakModel model = null;
        BufferedReader reader = null;
        String filePath = FileConfig.OUTBREAK_FILE_DIR_PATH + fileName;
        try {
            reader = new BufferedReader(new InputStreamReader(FileManager.getFileHandle(filePath).read()));
            int width = Integer.parseInt(reader.readLine());
            int height = Integer.parseInt(reader.readLine());
            Array<Vector2> placeList = new Array<Vector2>();
            for (int y = height-1; y >= 0; y--) {
                String line=reader.readLine();
                if (line == null) {
                    break;
                }
                for (int x=0; x<width; x++) {
                    if (line.charAt(x) == BALL_CHARACTER) {
                        placeList.add(new Vector2(x, y));
                    }
                }
            }
            Vector2[] places = placeList.toArray(Vector2.class);
            model = new MagicOutbreakModel(width, height, places);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileManager.closeReader(reader);
        }
        return model;
    }
}
