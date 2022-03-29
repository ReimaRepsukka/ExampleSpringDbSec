package com.dbsec.example.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dbsec.example.CustomerService;
import com.dbsec.example.data.Customer;
import com.dbsec.example.data.CustomerRepository;
import com.dbsec.example.data.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Base64;

@Service
public class CustomerSecurityService {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerPwEncoder encoder;
    @Autowired
    CustomerRepository customerRepo;

    @Value("${jwt.secret}")
    private String jwtSecret;


    public String registerUser(String username, String password, String role){

        Customer customer = new Customer(
            username,
            encoder.encode(username),
            Role.valueOf(role)
        );

        customerRepo.save(customer);

        return "Customer added";
    }

    /**
     * Checks basic authentication from basic auth header
     * @param basicAuthHeader
     * @return
     */
    public String checkBasicAuthentication(String basicAuthHeader){

        if(!basicAuthHeader.startsWith("Basic")){
            return null;
        }

        String cred = basicAuthHeader.substring("Basic".length()+1);
        cred = new String(Base64.getDecoder().decode(cred));  //username:password

        String[] info = cred.split(":");

        return checkAuthentication(info[0], info[1]);
    }

    /**
     * Method that authenticates and returns a jwt token if authenticated.
     * @param username
     * @param password
     * @return jwt token or null
     */
    public String checkAuthentication(String username, String password){
        Customer c = customerService.getCustomer(username);
        if(c == null){
            return null;
        }

        return encoder.matches(password, c.password ) ? createToken(c) : null;
    }

    /**
     * Creates jwt token from customer info
     * @param customer
     * @return jwt token
     */
    public String createToken(Customer customer){
        Algorithm alg = Algorithm.HMAC256(jwtSecret);

        return JWT.create()
                .withSubject(customer.username)
                .withClaim("role", customer.role.toString())
                .sign(alg);
    }

    /**
     * Validates the bearer header that contains jwt token
     * @param bearerHeader header containing bearer info  (Bearer jwttoken)
     * @return customer info from jwt payload or null if not valid token
     */
    public Customer validateBearerToken(String bearerHeader){
        if(bearerHeader.startsWith("Bearer")){
            String token = bearerHeader.substring( "Bearer".length() + 1 );
            return this.validateJwt(token);
        }
        return null;
    }

    /**
     * Validates jwt token string
     * @param jwtToken
     * @return customer info from jwt payload or null if not valid token
     */
    public Customer validateJwt(String jwtToken){
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(alg).build();

        Customer customer = null;

        try{
            DecodedJWT jwt = verifier.verify(jwtToken);

            customer = new Customer(
                    jwt.getSubject(),
                    null,
                    Role.valueOf(jwt.getClaim("role").asString()));

        }catch(JWTVerificationException e){}

        return customer;
    }

    /**
     * Extra method for ebabling CORS configuration if needed.
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
                "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type",
                "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
