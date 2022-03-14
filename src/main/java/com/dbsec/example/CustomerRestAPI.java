package com.dbsec.example;

import com.dbsec.example.data.Customer;
import com.dbsec.example.data.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerRestAPI {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customers")
    public Map<String, Object> getCustomers(){
        return customerService.getCustomCustomer("repe");
    }
}
