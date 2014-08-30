package com.aigirls.manager;

import com.aigirls.config.FileConfig;
import com.aigirls.config.ServerInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileManager {


    public static FileHandle getFileHandle(String filePath)
    {
        if (FileConfig.USE_INNER_FILE) {
            return Gdx.files.internal(filePath);
        } else {
            return Gdx.files.external(ServerInfo.FILE_SERVER_URL + filePath);
        }
    }
}
