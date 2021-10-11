package com.project.utopia.controller;


import com.project.utopia.entity.Announcement;
import com.project.utopia.holder.request.AnnouncementRequestBody;
import com.project.utopia.holder.request.DeleteAnnouncementRequestBody;
import com.project.utopia.holder.request.SetRequestStatusRequestBody;
import com.project.utopia.holder.request.UpdateAnnouncementRequestBody;
import com.project.utopia.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @RequestMapping(value = "/new-announcement", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postAnnouncement(@RequestBody AnnouncementRequestBody announcementRequestBody) {
        announcementService.saveAnnouncement(announcementRequestBody);
    }

    @RequestMapping(value = "/announcements", method = RequestMethod.GET)
    @ResponseBody
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @RequestMapping(value = "/announcements/deleteAnnouncement", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    public ResponseEntity<Object> deleteAnnouncements(@RequestBody List<DeleteAnnouncementRequestBody> deleteAnnouncementList) {
        int count = announcementService.deleteAnnouncements(deleteAnnouncementList);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateAnnouncement", method = RequestMethod.PATCH)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    public ResponseEntity<Object> updateAnnouncements(@RequestBody List<UpdateAnnouncementRequestBody> updateAnnouncementList) {
        //return number of announcement update operation made
        int count = announcementService.updateAnnouncement(updateAnnouncementList);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
