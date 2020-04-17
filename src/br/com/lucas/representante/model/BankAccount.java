package br.com.lucas.representante.model;

public class BankAccount {
    private String bankName;
    private int bankNumber;
    private int agency;
    private int account;

    private Client owner;

    public BankAccount(String bankName, int bankNumber, int agency, int account) {
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.agency = agency;
        this.account = account;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankName='" + bankName + '\'' +
                ", bankNumber=" + bankNumber +
                ", agency=" + agency +
                ", account=" + account +
                '}';
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(int bankNumber) {
        if(bankNumber > 0)
            throw new IllegalArgumentException("Bank number must be a positive integer.");
        this.bankNumber = bankNumber;
    }

    public int getAgency() {
        return agency;
    }

    public void setAgency(int agency) {
        if(agency > 0)
            throw new IllegalArgumentException("Agency number must be a positive integer.");
        this.agency = agency;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        if(account > 0)
            throw new IllegalArgumentException("Account number must be a positive integer.");
        this.account = account;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
}
