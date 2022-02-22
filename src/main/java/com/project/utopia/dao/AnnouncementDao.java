package com.project.utopia.dao;

import com.project.utopia.entity.Announcement;
import com.project.utopia.holder.request.DeleteAnnouncementRequestBody;
import com.project.utopia.holder.request.UpdateAnnouncementRequestBody;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
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
     *
     * @return int : number of announcements deleted
     */
    public int deleteAnnouncement(List<DeleteAnnouncementRequestBody> deleteAnnouncementList) {
        Session session = null;
        int deletedCount = 0;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            for (DeleteAnnouncementRequestBody item : deleteAnnouncementList) {
                System.out.println("Going to delete announcement!!! AnnouncementId: " + item.getAnnouncementId());
                Announcement announcementItem = session.get(Announcement.class, Integer.valueOf(item.getAnnouncementId()));
                session.delete(announcementItem);
                deletedCount++;
            }
            session.getTransaction().commit();
            System.out.println("Total deleted:" + deletedCount);
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


    /**
     * Apply announcement updates submitted by Admin in bulk
     *
     * @return int : number of announcement updates operations made
     */
    public int updateAnnouncement(List<UpdateAnnouncementRequestBody> updateAnnouncementRequestBodyList) {
        Session session = null;
        int count = 0;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            for (UpdateAnnouncementRequestBody item : updateAnnouncementRequestBodyList) {
                String qryString = "UPDATE Announcement announcement set announcement.title=:title, announcement.content=:content, announcement.category=:category where announcement.announcementId=:announcementId ";
                Query query = session.createQuery(qryString)
                        .setParameter("announcementId", item.getAnnouncementId())
                        .setParameter("title", item.getTitle())
                        .setParameter("content", item.getContent())
                        .setParameter("category", item.getCategory());
                count += query.executeUpdate();
            }
            session.getTransaction().commit();
            System.out.println("Total updated:" + count);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

}
