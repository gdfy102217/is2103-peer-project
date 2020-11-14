/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Partner;
import javax.ejb.Local;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.PartnerExistException;


@Local

public interface CustomerSessionBeanLocal {
    
    public Long createNewCustomer(Customer customer) throws CustomerExistException, GeneralException;
    
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Long createNewPartner(Partner partner) throws PartnerExistException, GeneralException;
    
}
