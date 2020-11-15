/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public FlightSchedulePlan createNewSingleFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight, 
            Date departureDateTime, Date durationTime) throws FlightSchedulePlanExistException, GeneralException;
    
    public FlightSchedulePlan createNewMultipleFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight, 
            List<Date> departureDateTimes, List<Date> durationTimes) throws FlightSchedulePlanExistException, GeneralException;
    
    public FlightSchedulePlan createNewRecurrentFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight, 
            Date departureDateTime, Date durationTime, Integer recurrence, Date endDateTime) throws FlightSchedulePlanExistException, GeneralException;
    
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();

    public List<FlightSchedulePlan> retrieveFlightSchedulePlansByFlightNumber(String flightNumber) throws FlightSchedulePlanNotFoundException;

    public Long createReturnFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight, Date layoverDurationTime) throws FlightSchedulePlanExistException,
            GeneralException, FlightScheduleExistException;
    
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException;
    
     public void updateFlightSchedulePlanFares(FlightSchedulePlan flightSchedulePlan, List<Fare> fares);
     
}
