package com.assignment.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {
    private String authToken;
    @Autowired
    CustomerService service;
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/")

    public String getLogin() {
        return "login";
    }
    @PostMapping("/authenticate")
            public String authenticateUser(@RequestParam("login_id") String loginId,@RequestParam("password") String password,Model model)
    {

        authToken=service.authenticateUser(loginId,password,model);
    return service.authenticateUser(loginId,password,model);

    }
    @GetMapping("/customer-list")
    public String customerList(Model model) {
        // Fetch and pass customer data to the template here
        return "customer-list";
    }

    @GetMapping("/add-customer")
    public String addCustomer(Model model) {
        model.addAttribute("customerForm", new CustomerForm());
        return "add-customer";
    }



}
