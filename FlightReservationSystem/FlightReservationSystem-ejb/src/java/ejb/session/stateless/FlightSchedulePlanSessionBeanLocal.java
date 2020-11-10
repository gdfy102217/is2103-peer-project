/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import javax.ejb.Local;
import util.exception.FlightSchedulePlanExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException;
    
}
