/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightScheduleNotFountException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightSchedulePlanException;

/**
 *
 * @author Administrator
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public FlightSchedulePlan createNewSingleFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight,
            Date departureDate, Date departureTime, Date durationTime) throws FlightSchedulePlanExistException, GeneralException;

    public FlightSchedulePlan createNewMultipleFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight,
            List<Date> departureDates, List<Date> departureTimes, List<Date> durationTimes) throws FlightSchedulePlanExistException, GeneralException;

    public FlightSchedulePlan createNewRecurrentFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight,
            Date departureTime, Date durationTime, Integer recurrence, Date startDateTime, Date endDateTime) throws FlightSchedulePlanExistException, GeneralException;

    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();

    public List<FlightSchedulePlan> retrieveFlightSchedulePlansByFlightNumber(String flightNumber) throws FlightSchedulePlanNotFoundException;

    public Long createReturnFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight, Date layoverDurationTime) throws FlightSchedulePlanExistException,
            GeneralException, FlightScheduleExistException;

    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException;

    public void updateFlightSchedulePlanFares(FlightSchedulePlan flightSchedulePlan, List<Fare> fares);

    public void updateSingleFlightSchedule(FlightSchedulePlan flightSchedulePlan, FlightSchedule flightSchedule, Date departureDate, Date departureTime, Date durationTime) throws UpdateFlightSchedulePlanException;

    public void updateRecurrentDayFlightSchedule(FlightSchedulePlan flightSchedulePlan, Integer recurrence, Date endDate) throws UpdateFlightSchedulePlanException;

    public void updateRecurrentWeekFlightSchedule(FlightSchedulePlan flightSchedulePlan, Date endDate) throws UpdateFlightSchedulePlanException;
    
}
