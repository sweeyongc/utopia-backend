package com.project.utopia.controller;

import com.project.utopia.entity.Customer;
import com.project.utopia.holder.response.AccountInfoResponseBody;
import com.project.utopia.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountInfoController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/account-info", method = RequestMethod.GET)
    public ResponseEntity<Object> accountInfo() {
        System.out.println("Enter the accountInfo controller");
        Customer customer = customerService.getCurrentCustomer();
        AccountInfoResponseBody response = new AccountInfoResponseBody();
        response.setEmail(customer.getUser().getEmailId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setAddress(customer.getAddress());
        response.setBalance(customer.getBalance());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
