package com.project.utopia.dao;

import com.project.utopia.entity.Customer;
import com.project.utopia.holder.request.SetRequestStatusRequestBody;
import com.project.utopia.holder.request.DeleteRequestRequestBody;
import org.springframework.stereotype.Repository;
import com.project.utopia.entity.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestDao {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CustomerDao customerDao;

    /**
     * Submit a request object to database
     *
     * @param request
     */
    public void save(Request request) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(request);
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

    /**
     * Get list of requests created by current customer
     *
     * @return List of Request objects
     */
    public List<Request> getCurrentUserRequests() {
        Customer customer = customerDao.getCurrentCustomer();
        if (customer != null) {
            return customer.getRequests();
        }
        return new ArrayList<>();
    }

    /**
     * Get all requests list for sorted by lastModified time, used by Admin
     *
     * @return List of Request objects
     */
    public List<Request> getAllRequests() {
        Session session = null;
        List<Request> requestList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            //Fetch requests for different status and append to request list
            //Requests of same status are sorted by lastModifiedTime
            TypedQuery<Request> openQuery = session.createQuery("SELECT request FROM Request request WHERE request.status = 'OPEN' ORDER BY request.lastModifiedTime", Request.class);
            requestList.addAll(openQuery.getResultList());
            TypedQuery<Request> inProgressQuery = session.createQuery("SELECT request FROM Request request WHERE request.status = 'IN PROGRESS' ORDER BY request.lastModifiedTime", Request.class);
            requestList.addAll(inProgressQuery.getResultList());
            TypedQuery<Request> resolvedQuery = session.createQuery("SELECT request FROM Request request WHERE request.status = 'RESOLVED' ORDER BY request.lastModifiedTime", Request.class);
            requestList.addAll(resolvedQuery.getResultList());
            return requestList;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return requestList;
    }

    /**
     * Apply requests status update submitted by Admin in bulk
     *
     * @return int : number of request status update operations made
     */
    public int setRequestsStatus(List<SetRequestStatusRequestBody> setStatusList) {
        Session session = null;
        int count = 0;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            for (SetRequestStatusRequestBody item : setStatusList) {
                System.out.println("Target requestId: " + item.getRequestId() + ", change status to: " + item.getStatus());

                String qryString = "UPDATE Request request set request.status=:status where request.id=:id";
                Query query = session.createQuery(qryString)
                        .setParameter("status", item.getStatus())
                        .setParameter("id", item.getRequestId());
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

    /**
     * Delete requests submitted by User/Admin in bulk
     *
     * @return int : number of request deleted
     */
    public int deleteRequest(List<DeleteRequestRequestBody> deleteRequestList) {
        Session session = null;
        int deletedCount = 0;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            for (DeleteRequestRequestBody item : deleteRequestList) {
                System.out.println("Going to delete request!!! RequestId: " + item.getRequestId());
                Request requestItem = session.get(Request.class, Integer.valueOf(item.getRequestId()));
                Customer customer = requestItem.getCustomer();
                customer.getRequests().remove(requestItem);
                session.delete(requestItem);
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

}