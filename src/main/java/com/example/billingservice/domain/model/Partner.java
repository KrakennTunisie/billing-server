package com.example.billingservice.domain.model;

import com.example.billingservice.domain.enums.PartnerType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Partner {

    private UUID idPartner;
    private String name;
    private String email;
    private String phoneNumber;
    private String taxRegistrationNumber;
    private String country;
    private String adress;
    private String iban;
    private Document rne;
    private Document contact;
    private Document patente;
    private PartnerType partnerType;

    private List<Invoice> invoices;


    public Partner(UUID idPartner, String name, String email, String phoneNumber,
                   String taxRegistrationNumber, String country, String adress, String iban,
                   Document rne, Document contact,Document patente , PartnerType partnerType) {
        this.idPartner = idPartner;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.taxRegistrationNumber = taxRegistrationNumber;
        this.country = country;
        this.adress = adress;
        this.iban = iban;
        this.rne = rne;
        this.contact = contact;
        this.patente = patente;
        this.partnerType = partnerType;
    }

    public Partner() {
    }

    public UUID getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(UUID idPartner) {
        this.idPartner = idPartner;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTaxRegistrationNumber() {
        return taxRegistrationNumber;
    }

    public void setTaxRegistrationNumber(String taxRegistrationNumber) {
        this.taxRegistrationNumber = taxRegistrationNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Document getRne() {
        return rne;
    }

    public void setRne(Document rne) {
        this.rne = rne;
    }

    public Document getContact() {
        return contact;
    }

    public void setContact(Document contact) {
        this.contact = contact;
    }

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public Document getPatente() {
        return patente;
    }

    public void setPatente(Document patente) {
        this.patente = patente;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Partner partner)) return false;
        return Objects.equals(taxRegistrationNumber, partner.taxRegistrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taxRegistrationNumber);
    }

    @Override
    public String toString() {
        return "Partner{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", taxRegistrationNumber='" + taxRegistrationNumber + '\'' +
                ", country='" + country + '\'' +
                ", adress='" + adress + '\'' +
                ", iban=" + iban +
                ", rne=" + rne +
                ", contact=" + contact +
                ", partnerType=" + partnerType +
                '}';
    }
}
