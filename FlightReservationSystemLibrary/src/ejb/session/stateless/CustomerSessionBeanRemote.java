/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;


@Remote

public interface CustomerSessionBeanRemote {
    
    public Long createNewCustomer(Customer customer) throws CustomerExistException, GeneralException;
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;
    
}
