package com.dbsec.example.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {
    public List<CustomerInfo> findByCustomerUsername(String username);
}
