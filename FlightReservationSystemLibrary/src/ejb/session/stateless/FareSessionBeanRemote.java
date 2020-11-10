/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import javax.ejb.Remote;
import util.exception.FareExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FareSessionBeanRemote {
    
    public Fare createNewFare(Fare fare) throws FareExistException, GeneralException;
    
}
