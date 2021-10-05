package com.project.utopia.service;

import com.project.utopia.dao.RequestDao;
import com.project.utopia.entity.Customer;
import com.project.utopia.entity.Request;
import com.project.utopia.holder.request.NewRequestRequestBody;
import com.project.utopia.holder.request.SetRequestStatusRequestBody;
import com.project.utopia.holder.request.DeleteRequestRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestDao requestDao;

    @Autowired
    private CustomerService customerService;


    public void submitRequest(NewRequestRequestBody requestBody) {

        Customer customer = customerService.getCurrentCustomer();

        Request requestObject = new Request();
        requestObject.setCustomer(customer);
        requestObject.setEmailId(customer.getUser().getEmailId());

        requestObject.setTitle(requestBody.getTitle());
        requestObject.setContent(requestBody.getContent());
        requestObject.setCategory(requestBody.getCategory());
        requestObject.setPhotoURL(requestBody.getPhotoURL());
        requestObject.setStatus("Open");
        long now = System.currentTimeMillis();
        requestObject.setCreationTime(now);
        requestObject.setLastModifiedTime(now);
        requestDao.save(requestObject);
    }

    public List<Request> getCurrentRequests(){
        return requestDao.getCurrentUserRequests();
    }

    public List<Request> getAllRequests() {
        return requestDao.getAllRequests();
    }

    public int setRequestsStatus(List<SetRequestStatusRequestBody> setStatusList) {
        return requestDao.setRequestsStatus(setStatusList);
    }

    public int deleteRequest(List<DeleteRequestRequestBody> deleteRequestList) {
        return requestDao.deleteRequest(deleteRequestList);
    }
}
