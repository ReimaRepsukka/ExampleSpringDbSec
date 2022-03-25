package com.dbsec.example;

import com.dbsec.example.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    CustomerInfoRepository infoRepo;

    @PostConstruct
    public void init(){

    }

    public List<CustomerInfo> getCustomerInfos(String username){
        return infoRepo.findByCustomerUsername(username);
    }

    public Map<String, Object> getCustomCustomer(String username){
        Customer c = this.getCustomer(username);

        List<CustomerInfo> infos = this.getCustomerInfos(c.username);

        List<String> messages = new ArrayList<>();
        for(CustomerInfo info : infos){
            messages.add(info.message);
        }

        Map<String, Object> json = new HashMap<>();
        json.put("username", c.username);
        json.put("messages", messages);

        return json;
    }

    public Customer getCustomer(String username){
        return customerRepo.findById(username).orElse(null);
    }

    public List<Customer> getCustomers(){
        return customerRepo.findAll();
    }
}
