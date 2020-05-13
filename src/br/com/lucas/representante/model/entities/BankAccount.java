package br.com.lucas.representante.model.entities;

public class BankAccount {
    private String bankName;
    private String bankNumber;
    private String agency;
    private String account;

    private Client owner;

    public BankAccount(String bankName, String bankNumber, String agency, String account) {
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.agency = agency;
        this.account = account;
    }

    public BankAccount() {

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

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
}
