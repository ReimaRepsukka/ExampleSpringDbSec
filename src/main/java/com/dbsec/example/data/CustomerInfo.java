package com.dbsec.example.data;

import javax.persistence.*;

@Entity
@Table(name="info")
public class CustomerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name="message")
    public String message;

    @Column(name="customer")
    public String customerUsername;

    public CustomerInfo(String message, String customerUsername) {
        this.message = message;
        this.customerUsername = customerUsername;
    }

    public CustomerInfo(){}
}
