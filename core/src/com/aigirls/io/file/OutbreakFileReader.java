package com.aigirls.io.file;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.FileManager;
import com.aigirls.model.MagicOutbreakModel;

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
            List<Point> placeList = new ArrayList<Point>();
            for (int y=0; y<height; y++) {
                String line=reader.readLine();
                if (line == null) {
                    break;
                }
                for (int x=0; x<width; x++) {
                    if (line.charAt(x) == BALL_CHARACTER) {
                        placeList.add(new Point(x, y));
                    }
                }
            }
            Point[] places = (Point[])placeList.toArray(new Point[placeList.size()]);
            model = new MagicOutbreakModel(width, height, places);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileManager.closeReader(reader);
        }
        return model;
    }
}
