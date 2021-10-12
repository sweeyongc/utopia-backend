package com.project.utopia.dao;

import com.project.utopia.entity.Authorities;
import com.project.utopia.entity.Customer;
import com.project.utopia.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import com.project.utopia.holder.request.RegisterRequestBody;

@Repository
public class CustomerDao {
    @Autowired
    private SessionFactory sessionFactory;

    public int addCustomer(RegisterRequestBody request) {
        User user = new User();   // build a user from request
        user.setEnabled(true);
        user.setEmailId(request.getEmail());
        user.setPassword(request.getPassword());

        Customer customer = new Customer(); // build a customer from request
        customer.setAddress(request.getAddress());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setUser(user);

        Authorities authorities = new Authorities();  // build a authorities from request
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmailId(request.getEmail());
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(customer);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return -1; // on error
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }

    public Customer getCurrentCustomer() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String emailId = loggedInUser.getName();

        User user = null;
        try (Session session = sessionFactory.openSession()) {
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("emailId", emailId)).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            return user.getCustomer();
        }
        return null;
    }
}
