package sec15q_challenge_6789;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private double balance;
    private String accountNumber;

    private Lock lock;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    public void deposit(double amount) {

        boolean status = false;

        try {
            if(lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    balance += amount;
                    status = true;
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Could not get the lock!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Transaction status = " + status);
    }

    public void withdraw(double amount) {

        boolean status = false;

        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    balance -= amount;
                    status = true;
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Could not get the lock!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Transaction status = " + status);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void printAccountNumber() {
        System.out.println("Account number = " + accountNumber);
    }

    public double getBalance() {
        return balance;
    }
}

/*
*
* CHALLENGE 6 - update code so that the "status" variable is thread safe
*
* (local variables are stored on the thread stack so each thread will have its own copy (already thread safe))
*
 */