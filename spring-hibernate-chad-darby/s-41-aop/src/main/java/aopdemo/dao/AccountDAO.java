package aopdemo.dao;

import org.springframework.stereotype.Component;
import aopdemo.dependency.Account;

import java.util.ArrayList;
import java.util.List;


@Component
public class AccountDAO {

    private String name;
    private String serviceCode;


    public void addAccount(Account account) {
        System.out.println(getClass() + ": ADDING AN ACCOUNT...");
    }

    public void addAccount(Account account, boolean vipFlag) {
        System.out.println(getClass() + ": ADDING AN ACCOUNT...");
    }

    public String getName() {
        System.out.println(getClass() + ": in getName()");
        return name;
    }

    public void setName(String name) {
        System.out.println(getClass() + ": in setName()");
        this.name = name;
    }

    public String getServiceCode() {
        System.out.println(getClass() + ": in getServiceCode()");
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        System.out.println(getClass() + ": in setServiceCode()");
        this.serviceCode = serviceCode;
    }

    public List<Account> findAccounts() {
        List<Account> myAccounts = new ArrayList<>();

        // create some sample accounts
        Account temp1 = new Account("John", "Silver");
        Account temp2 = new Account("Fred", "Platinum");
        Account temp3 = new Account("Bob", "Gold");

        myAccounts.add(temp1);
        myAccounts.add(temp2);
        myAccounts.add(temp3);

        return myAccounts;
    }
}
