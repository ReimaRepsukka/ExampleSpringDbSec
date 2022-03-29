package com.dbsec.example;

import com.dbsec.example.data.Customer;
import com.dbsec.example.security.CustomerSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
public class CustomerRestAPI {

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerSecurityService customerSecurity;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Map<String,String> credentials){
        
        customerSecurity.registerUser(
            credentials.get("username"),
            credentials.get("password"),
            credentials.get("role")
        );

        return new ResponseEntity<>( "User registered", HttpStatus.OK );
    }

    /**
     * Basic authentication login
     * @param basicAuthHeader Basic username:password header where the credentials are  BASE64 coded.
     * @return unauthorized status or json {"token"} if succeed
     */
    @PostMapping("/loginbasic")
    public ResponseEntity<Map<String,String>> loginBasic(@RequestHeader("Authorization") String basicAuthHeader){

        String token = customerSecurity.checkBasicAuthentication(basicAuthHeader);

        if(token == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>( Map.of( "token", token ), HttpStatus.OK );
    }

    /**
     * Json login
     * @param credentials credentials in json {"username, "password"}
     * @return unauthorized status or json {"token", token} if succeed
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> credentials){
        String token = customerSecurity.checkAuthentication(
                credentials.get("username"),
                credentials.get("password"));

        if(token == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>( Map.of( "token", token ), HttpStatus.OK );
    }

    /**
     * Login with query parameters
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/loginform")
    public ResponseEntity<Map<String,String>> loginForm(@RequestParam String username, @RequestParam String password){
        String token = customerSecurity.checkAuthentication(username,password);

        if(token == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>( Map.of( "token", token ), HttpStatus.OK );
    }


    /**
     * Private resource that is accessible only if the token is verified
     * @param bearer JWT token in header   Authorization: Bearer jwttoken
     * @return authenticated customer info or forbidden status
     */
    @GetMapping("/private")
    public ResponseEntity<Customer> getPrivate(@RequestHeader("Authorization") String bearer){
        Customer c = customerSecurity.validateBearerToken(bearer);
        if( c== null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @GetMapping("/customers")
    public Map<String, Object> getCustomers(){
        return customerService.getCustomCustomer("repe");
    }
}
