/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import javax.ejb.Remote;
import util.exception.FlightSchedulePlanExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {
    
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException;
    
}
