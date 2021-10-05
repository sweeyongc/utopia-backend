package com.project.utopia.dao;

import com.project.utopia.entity.Announcement;
import com.project.utopia.holder.request.DeleteAnnouncementRequestBody;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnnouncementDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void saveAnnouncement(Announcement announcement) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(announcement);
            session.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Announcement getAnnouncementById(int announcementId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Announcement.class, announcementId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcementList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Announcement> openQuery = session.createQuery("SELECT announcements FROM Announcement announcements ORDER BY announcements.announcementId desc", Announcement.class);
            announcementList.addAll(openQuery.getResultList());
            return announcementList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return announcementList;
    }

    /**
     * Delete announcements submitted by Admin in bulk
     * @return int : number of announcements deleted
     */
    public int deleteAnnouncement(List<DeleteAnnouncementRequestBody> deleteAnnouncementList){
        Session session = null;
        int deletedCount = 0;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            for ( DeleteAnnouncementRequestBody item: deleteAnnouncementList ){
                System.out.println("Going to delete announcement!!! AnnouncementId: " + item.getAnnouncementId());
                Announcement announcementItem = session.get(Announcement.class, Integer.valueOf(item.getAnnouncementId()));
//                Customer customer = announcementItem.getCustomer();
//                //actually removing requestItem from "request" table
//                customer.getRequests().remove(requestItem);
                session.delete(announcementItem);
                deletedCount++;
            }
            session.getTransaction().commit();
            System.out.println("Total deleted:"  + deletedCount);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return deletedCount;
    }

}
