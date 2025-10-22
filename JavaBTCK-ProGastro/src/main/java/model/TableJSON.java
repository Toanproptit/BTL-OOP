package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableJSON {

    private static final String FILE_PATH = "data/table.json";
    private static List<Table> tableList;

    static {
        try {
            tableList=loadTable();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void saveTable() throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(tableList, writer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static List<Table> loadTable() throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        if (!file.exists()) return new ArrayList<>();

        try (FileReader reader = new FileReader(FILE_PATH)) {
            List<Table> list = new Gson().fromJson(reader, new TypeToken<List<Table>>() {}.getType());
            if (list == null) return new ArrayList<>();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static void addTable(Table table){
        tableList.add(table);
        try {
            saveTable();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void updateTable1(Table table){
        int i= table.getId();
        tableList.set(i,table);
        try {
            saveTable();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void updateTable(Table table){
        for(int i=0;i<tableList.size();++i){
            if(table.getName().equals(tableList.get(i).getName())){
                tableList.set(i,table);
                break;
            }
        }
        try {
            saveTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void earseTable(Table table) throws IOException {
        tableList.remove(table.getId());
        saveTable();
    }

    public static List<Table> getTableList() {
        return tableList;
    }
}
