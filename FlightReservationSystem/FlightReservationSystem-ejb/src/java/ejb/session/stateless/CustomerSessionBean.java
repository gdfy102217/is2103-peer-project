/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.PartnerExistException;


@Stateless

public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    

    @Override
    public Long createNewCustomer(Customer customer) throws CustomerExistException, GeneralException {
        try {
            em.persist(customer);
            em.flush();
        } catch(PersistenceException ex) {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new CustomerExistException("Customer with same username already exist"); 
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        return customer.getCustomerId();
    }
    
    @Override
    public Long createNewPartner(Partner partner) throws PartnerExistException, GeneralException {
        try {
            em.persist(partner);
            em.flush();
        } catch(PersistenceException ex) {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new PartnerExistException("Customer with same username already exist"); 
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        return partner.getCustomerId();
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = ?1");
        query.setParameter(1, username);
        
        try {
            Customer customer = (Customer)query.getSingleResult();
            customer.getFlightReservations().size();
            
            return customer;
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with username: " + username + " does not exist");
        }
    }
}
