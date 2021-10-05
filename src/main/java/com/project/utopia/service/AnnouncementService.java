package com.project.utopia.service;


import com.project.utopia.dao.AnnouncementDao;
import com.project.utopia.entity.Announcement;
import com.project.utopia.holder.request.*;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;

    public void saveAnnouncement(AnnouncementRequestBody requestBody){
        Announcement announcementObject = new Announcement();
        announcementObject.setTitle(requestBody.getTitle());
        announcementObject.setCategory(requestBody.getCategory());
        announcementObject.setContent(requestBody.getContent());
        announcementObject.setCreationTime(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        announcementDao.saveAnnouncement(announcementObject);
    }

    public String getContent(int announcementId) {
        return getAnnouncementById(announcementId).getContent();
    }

    public Announcement getAnnouncementById(int announcementId) {
        return announcementDao.getAnnouncementById(announcementId);
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = announcementDao.getAllAnnouncements();
        return announcements;
    }

    public int deleteAnnouncements(List<DeleteAnnouncementRequestBody> deleteAnnouncementList) {
        return announcementDao.deleteAnnouncement(deleteAnnouncementList);
    }

    public int updateAnnouncement(List<UpdateAnnouncementRequestBody> updateAnnouncementRequestBodyList) {
        return announcementDao.updateAnnouncement(updateAnnouncementRequestBodyList);
    }
}
