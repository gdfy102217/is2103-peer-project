/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;


@WebService(serviceName = "PartnerEntityWebService")
@Stateless()
public class PartnerEntityWebService {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username) throws CustomerNotFoundException, GeneralException {
       
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = ?1");
        query.setParameter(1, username);
        
        try {
            Customer customer = (Customer)query.getSingleResult();
            customer.getFlightReservations().size();
            
            if (customer instanceof Partner) {
                Partner partner = (Partner)customer;
                
                partner.getFlightReservations().size();
                return partner;
            } else throw new GeneralException("User is not a registerd partner!");

        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with username: " + username + " does not exist");
        }
    }
}
