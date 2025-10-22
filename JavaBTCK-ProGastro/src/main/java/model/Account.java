package model;


public class Account {
    public enum Role { ADMIN, STAFF }


    private String name;
    private String phoneNumber;
    private String account;
    private String password;
    private Role role = Role.STAFF;

    public Account(String name,String phoneNumber,String account,String password){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.account=account;
        this.password=password;
    }

    public Account(String name, String phoneNumber, String account, String password, Role role) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.account = account;
        this.password = password;
        this.role = (role == null ? Role.STAFF : role);
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

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}



    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", role=" + role +
                '}';
    }
}
