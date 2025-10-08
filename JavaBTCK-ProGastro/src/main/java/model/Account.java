package model;

public class Account {
    private String name;
    private String phoneNumber;
    private String account;
    private String password;

    public Account(String name,String phoneNumber,String account,String password){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.account=account;
        this.password=password;
    }
    public String getPassword() {
        return password;
    }

    public String getAccount() {
        return account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
