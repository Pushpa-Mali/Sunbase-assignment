package com.assignment.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {

    CustomerRepository repo=new CustomerRepository();
    private String authToken;
    @Value("${api.baseurl}")
    private String apiUrl; // Define this in your application.properties

    private final RestTemplate restTemplate;


    @Autowired
    public CustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String authenticateUser(String loginId, String password, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"login_id\":\"" + loginId + "\",\"password\":\"" + password + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/assignment_auth.jsp", HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                authToken = response.getBody();
                return "redirect:/customer-list";
            } else {
                model.addAttribute("error", "Authentication failed. Please check your credentials.");
                return "login";
            }
        } catch (HttpClientErrorException.Unauthorized unauthorizedException) {
            throw new RuntimeException("Authentication failed: Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public ResponseEntity<String> getCustomerList(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/assignment.jsp?cmd=get_customer_list", HttpMethod.GET, entity, String.class);
            return response;
        } catch (HttpClientErrorException.Unauthorized unauthorizedException) {
            throw new RuntimeException("Authorization failed: Invalid token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve customer list: " + e.getMessage());
        }
    }

    public ResponseEntity<String> createCustomer(String authToken, CustomerForm customerRequest) {
        // API URL for creating a new customer
        String createCustomerApiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

        // Prepare the request headers with the Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);

        // Prepare the request body using the provided customer data
        HttpEntity<CustomerForm> entity = new HttpEntity<>(customerRequest, headers);

        // Make a POST request to create a new customer
        ResponseEntity<String> response = restTemplate.exchange(
                createCustomerApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }


}
