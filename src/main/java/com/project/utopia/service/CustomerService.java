package com.project.utopia.service;

import com.project.utopia.dao.CustomerDao;
import com.project.utopia.entity.Customer;
import com.project.utopia.holder.request.RegisterRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerDao customerDao;

    public int addCustomer(RegisterRequestBody request) {
        return customerDao.addCustomer(request);
    }

    public Customer getCurrentCustomer() {
        return customerDao.getCurrentCustomer();
    }
}
