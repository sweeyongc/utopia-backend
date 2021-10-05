package com.project.utopia.controller;


import com.project.utopia.entity.Announcement;
import com.project.utopia.holder.request.AnnouncementRequestBody;
import com.project.utopia.holder.request.DeleteAnnouncementRequestBody;
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

    @RequestMapping(value = "/announcements/new-announcement", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postAnnouncement(@RequestBody AnnouncementRequestBody announcementRequestBody) {
        announcementService.saveAnnouncement(announcementRequestBody);
    }

    @RequestMapping(value = "/announcements", method = RequestMethod.GET)
    @ResponseBody
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @RequestMapping(value = "/announcements/{announcement-id}", method = RequestMethod.GET)
    @ResponseBody
    public Announcement getAnnouncementById(@PathVariable("announcement-id") int announcementId) {
       return announcementService.getAnnouncementById(announcementId);
    }

    @RequestMapping(value = "/announcements/content/{announcement-id}", method = RequestMethod.GET)
    @ResponseBody
    public String getContent(@PathVariable("announcement-id") int announcementId) {
        return announcementService.getContent(announcementId);
    }

    @RequestMapping(value = "/announcements/deleteAnnouncement", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    public ResponseEntity<Object> deleteAnnouncements(@RequestBody List<DeleteAnnouncementRequestBody> deleteAnnouncementList) {
        int count = announcementService.deleteAnnouncements(deleteAnnouncementList);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
