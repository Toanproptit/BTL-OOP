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
    private static final String FILE_PATH = "account.json";
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
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return new Gson().fromJson(reader, new TypeToken<List<Account>>() {}.getType());
        } catch (IOException e) {
            accountList = new ArrayList<>();
        }
        return List.of();
    }
    public static void saveAccounts() throws IOException {
        File file = new File(FILE_PATH);
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