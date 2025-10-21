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


public class AccountJSON {
    private static final String FILE_PATH = "data/account.json";
    private static List<Account> accountList;
    static {
        try {
            accountList = loadAccounts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Account> loadAccounts() throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(FILE_PATH)) {
            List<Account> list = new Gson().fromJson(reader, new TypeToken<List<Account>>() {}.getType());
            if (list == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(list);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static void saveAccounts() throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(accountList, writer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void addAccount(Account account) {
        accountList.add(account);
        try {
            saveAccounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Account> getAccountList() {
        return accountList;
    }
}