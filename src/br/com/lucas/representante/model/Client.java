package br.com.lucas.representante.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Client {
    private String companyName;
    private String tradeName;
    private String companyId;
    private String cnpjOrCpf;
    private String stateRegistration;
    private String cityRegistration;
    private LocalDate clientSince;
    private String phone1;
    private String phone2;
    private String fax;

    private BankAccount account;
    private Address address;
    private List<Contact> contacts = new ArrayList<>();

    public Client(String companyName, String tradeName, String companyId, String cnpjOrCpf, String stateRegistration,
                  String cityRegistration, LocalDate clientSince, String phone1, String phone2, String fax) {
        this(companyName, tradeName, companyId, cnpjOrCpf, stateRegistration, cityRegistration,
                clientSince, phone1, phone2, fax, null, null);
    }

    public Client(String companyName, String tradeName, String companyId, String cnpjOrCpf, String stateRegistration,
                  String cityRegistration, LocalDate clientSince, String phone1, String phone2, String fax,
                  BankAccount account, Address address) {

        this.companyName = companyName;
        this.tradeName = tradeName;
        this.companyId = companyId;
        this.cnpjOrCpf = cnpjOrCpf;
        this.stateRegistration = stateRegistration;
        this.cityRegistration = cityRegistration;
        this.clientSince = clientSince;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.fax = fax;
        this.account = account;
        this.address = address;

        if(account != null )
            this.account.setOwner(this);
        if(address != null)
            this.address.setResident(this);
    }

    public boolean matchesSearchName(String substring){
        String tradeNameLowerCase = tradeName.toLowerCase();
        String companyNameLowerCase = companyName.toLowerCase();
        String subStringLowerCase = substring.toLowerCase();

        boolean isContainedInTradeName = tradeNameLowerCase.contains(subStringLowerCase);
        boolean isContainedInCompanyName = companyNameLowerCase.contains(subStringLowerCase);
        return isContainedInCompanyName || isContainedInTradeName;
    }

    public void addContact(@NotNull Contact c){
        contacts.add(c);
        c.setCompany(this);
    }

    public void removeContactIfExists(String name){
        Contact contactToRemove = getContactOrNull(name);
        removeContactIfExists(contactToRemove);
    }

    public void removeContactIfExists(@NotNull Contact c){
        contacts.remove(c);
        c.setCompany(null);
    }

    private Contact getContactOrNull(String name) {
        for (Contact contact : contacts)
            if(contact.getName().equals(name))
                return contact;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return companyName.equals(client.companyName) &&
                tradeName.equals(client.tradeName) &&
                Objects.equals(companyId, client.companyId) &&
                Objects.equals(cnpjOrCpf, client.cnpjOrCpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, tradeName, companyId, cnpjOrCpf);
    }

    @Override
    public String toString() {
        return "Client{" +
                "companyName='" + companyName + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", cnpjOrCpf='" + cnpjOrCpf + '\'' +
                '}';
    }

    public String getCompanyName(){
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCnpjOrCpf() {
        return cnpjOrCpf;
    }

    public void setCnpjOrCpf(String cnpjOrCpf) {
        this.cnpjOrCpf = cnpjOrCpf;
    }

    public String getStateRegistration() {
        return stateRegistration;
    }

    public void setStateRegistration(String stateRegistration) {
        this.stateRegistration = stateRegistration;
    }

    public String getCityRegistration() {
        return cityRegistration;
    }

    public void setCityRegistration(String cityRegistration) {
        this.cityRegistration = cityRegistration;
    }

    public LocalDate getClientSince() {
        return clientSince;
    }

    public String getClientSinceAsString() {
        String yearMonthDay = clientSince.getYear() +"/"
                + clientSince.getMonth() +
                "/"+ clientSince.getDayOfMonth();
        return yearMonthDay;
    }

    public void setClientSince(LocalDate clientSince) {
        this.clientSince = clientSince;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
        if(account != null )
            this.account.setOwner(this);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if(address != null )
            this.address.setResident(this);
    }

    public String getState(){
        return "Cliente Ativo";
    }
}
