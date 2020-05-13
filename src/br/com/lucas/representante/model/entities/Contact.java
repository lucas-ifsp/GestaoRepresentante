package br.com.lucas.representante.model.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
    private String name;
    private String email;
    private String position;
    private String phone;
    private String cpf;
    private String rg;
    private LocalDate birthday;

    private Client company;

    public Contact(String name, String email, String position, String phone, String cpf, String rg, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.phone = phone;
        this.cpf = cpf;
        this.rg = rg;
        this.birthday = birthday;
    }

    public Contact() {

    }

    public boolean matchesSearchNameOrEmail(String substring){
        boolean isContainedInName = name.contains(substring);
        boolean isContainedInEmail = email.contains(substring);
        return isContainedInName || isContainedInEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return name.equals(contact.name) &&
                email.equals(contact.email) &&
                Objects.equals(position, contact.position) &&
                Objects.equals(phone, contact.phone) &&
                Objects.equals(cpf, contact.cpf) &&
                Objects.equals(rg, contact.rg) &&
                Objects.equals(birthday, contact.birthday) &&
                Objects.equals(company, contact.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, position, phone, cpf, rg, birthday, company);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", phone='" + phone + '\'' +
                ", company name=" + company.getTradeName() +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Client getCompany() {
        return company;
    }

    public void setCompany(Client company) {
        this.company = company;
    }
}
