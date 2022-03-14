package com.dbsec.example.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="customer")
public class Customer{
    @Id
    @Column(name="username")
    public String username;

    @JsonIgnore
    @Column(name="password")
    public String password;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    public Role role;

    public Customer(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Customer(){}
}
