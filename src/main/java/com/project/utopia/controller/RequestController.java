package com.project.utopia.controller;

import com.project.utopia.entity.Request;
import com.project.utopia.holder.request.NewRequestRequestBody;
import com.project.utopia.holder.request.SetRequestStatusRequestBody;
import com.project.utopia.holder.request.DeleteRequestRequestBody;
import org.springframework.http.ResponseEntity;
import com.project.utopia.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @RequestMapping(value = "/new-request", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void submitRequest(@RequestBody NewRequestRequestBody request) {
        requestService.submitRequest(request);
    }

    @RequestMapping(value = "/current-requests", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getCurrentRequests() {
        List<Request> requestList = requestService.getCurrentRequests();
        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-request", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    public ResponseEntity<Object> deleteRequests(@RequestBody List<DeleteRequestRequestBody> deleteRequestList) {
        int count = requestService.deleteRequest(deleteRequestList);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @RequestMapping(value = "/request-management/all-requests", method = RequestMethod.GET)
    public ResponseEntity<Object> getOpenRequests() {
        List<Request> requestList = requestService.getAllRequests();
        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }

    @RequestMapping(value = "/request-management/set-request-status", method = RequestMethod.PATCH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    public ResponseEntity<Object> solveRequests(@RequestBody List<SetRequestStatusRequestBody> setStatusList) {
        //return number of request status update operation made
        int count = requestService.setRequestsStatus(setStatusList);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
