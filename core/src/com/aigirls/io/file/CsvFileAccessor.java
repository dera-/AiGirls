package com.aigirls.io.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aigirls.entity.BaseEntity;
import com.aigirls.manager.FileManager;
import com.badlogic.gdx.files.FileHandle;

public abstract class CsvFileAccessor {
    protected FileHandle csvFileHandle;
    protected static final String DELIMITER = ",";
    protected static final int NOT_FOUND_COLUMN = -1;
    protected static final String ID_COLUMN_NAME = "id";


    public CsvFileAccessor(String filePath)
    {
        csvFileHandle = FileManager.getFileHandle(filePath);
    }

    public BaseEntity getRecordById(String id) {
        BaseEntity[] records = getRecords(ID_COLUMN_NAME, id);
        return records.length==0 ? null : records[0];
    }

    public BaseEntity[] getRecords(String columnName, String columnValue) {
        List<BaseEntity> list = new ArrayList<BaseEntity>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(csvFileHandle.reader());
            int index = getColumnIndex(reader.readLine().split(DELIMITER), columnName);
            if(index == NOT_FOUND_COLUMN){
                return new BaseEntity[0];
            }
            String line;
            while ((line=reader.readLine()) != null) {
                String[] record = line.split(DELIMITER);
                if (record[index].equals(columnValue)) {
                    list.add(getBaseEntity(record));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileManager.closeReader(reader);
        }
        return exchangeToArray(list);
    }

    public BaseEntity[] getAllRecords() {
        List<BaseEntity> list = new ArrayList<BaseEntity>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(csvFileHandle.reader());
            String line;
            while ((line=reader.readLine()) != null) {
                list.add(getBaseEntity(line.split(DELIMITER)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileManager.closeReader(reader);
        }
        return exchangeToArray(list);
    }

    private int getColumnIndex(String[] columnNames, String targetName) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(targetName)) {
                return i;
            }
        }
        return NOT_FOUND_COLUMN;
    }

    private BaseEntity[] exchangeToArray(List<BaseEntity> list) {
        return (BaseEntity[])list.toArray(new BaseEntity[list.size()]);
    }

    protected abstract BaseEntity getBaseEntity(String[] record);


}
