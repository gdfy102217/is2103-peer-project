/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import javax.ejb.Local;
import util.exception.FareExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FareSessionBeanLocal {

    public Fare createNewFare(Fare fare) throws FareExistException, GeneralException;

    public void deleteFare(Fare fare);
    
}
