/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import javax.ejb.Local;
import util.exception.FlightScheduleExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightScheduleSessionBeanLocal {

    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException;
    
}
