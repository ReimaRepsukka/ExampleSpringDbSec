package com.dbsec.example.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

    final String query1 = "select * from customer where customer.username like %?1%";
    final String query2 = "select customer.username as customer, taulu.role as role from customer";

    public List<Customer> findByRole(Role role);

    @Query(value=query1, nativeQuery = true)
    public List<Customer> getCustomersBySearch(String s);

    @Query(value=query2, nativeQuery = true)
    public List<DtoCustom> getSpessu();
}
