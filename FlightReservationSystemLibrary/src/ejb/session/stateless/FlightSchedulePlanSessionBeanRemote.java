/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {
    
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException;
    
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();
    
    public List<FlightSchedulePlan> retrieveFlightSchedulePlansByFlightNumber(String flightNumber) throws FlightSchedulePlanNotFoundException;
    
    public void createReturnFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Date layoverDurationTime) throws FlightSchedulePlanExistException, GeneralException, FlightScheduleExistException;
    
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException;
    
}
