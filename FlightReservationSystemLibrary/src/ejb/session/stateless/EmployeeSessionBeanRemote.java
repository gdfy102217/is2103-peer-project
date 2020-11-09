/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Administrator
 */
@Remote
public interface EmployeeSessionBeanRemote {
    
    public Employee createNewEmployee(Employee employee) throws EmployeeExistException, GeneralException;
    
    public Employee employeeLogin(String username, String password) throws EmployeeNotFoundException, InvalidLoginCredentialException;
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
}
