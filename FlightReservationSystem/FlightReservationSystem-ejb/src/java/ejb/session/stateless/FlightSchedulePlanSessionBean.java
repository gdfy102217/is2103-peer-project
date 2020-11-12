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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.FlightScheduleType;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanExistException, GeneralException {
        try {
            em.persist(flightSchedulePlan);
            flightSchedulePlan.getFlight().getFlightSchedulePlans().add(flightSchedulePlan);
            em.flush();

            return flightSchedulePlan;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new FlightSchedulePlanExistException("This flight schedule plan already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans() {
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp ORDER BY fsp.flight.flightNumber ASC, fsp.firstDepartureTimeLong DESC");
        
        return query.getResultList();
    }

    @Override
    public List<FlightSchedulePlan> retrieveFlightSchedulePlansByFlightNumber(String flightNumber) throws FlightSchedulePlanNotFoundException {
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp WHERE fsp.flight.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);

        if (!query.getResultList().isEmpty()) {
            return query.getResultList();
        } else {
            throw new FlightSchedulePlanNotFoundException("Flight schedule plan with flight number " + flightNumber + " is not found!");
        }
    }

    @Override
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException {
        if (flightSchedulePlan.getFlightSchedules().isEmpty()) {
            flightSchedulePlan.getFlight().getFlightSchedulePlans().remove(flightSchedulePlan);
            flightSchedulePlan.setFlight(new Flight());
            for (Fare fare : flightSchedulePlan.getFares()) {
                fare.setFlightSchedulePlan(new FlightSchedulePlan());
                flightSchedulePlan.getFares().remove(fare);
            }
            em.remove(flightSchedulePlan);
        } else {
            flightSchedulePlan.setDisabled(true);
            System.out.println("Flight no. " + flightSchedulePlan.getFlight().getFlightNumber() + " is set disabled!");
            throw new DeleteFlightSchedulePlanException("Flight Schedule Plan with Flight no. " + flightSchedulePlan.getFlight().getFlightNumber() + " is in use and cannot be deleted!");
        }
    }

    @Override
    public void createReturnFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Date layoverDurationTime) throws FlightSchedulePlanExistException,
            GeneralException, FlightScheduleExistException {
        FlightSchedulePlan returnFlightSchedulePlan = new FlightSchedulePlan();

        returnFlightSchedulePlan.setFlight(newFlightSchedulePlan.getFlight());
        returnFlightSchedulePlan.setFlightScheduleType(newFlightSchedulePlan.getFlightScheduleType());
        if(returnFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYDAY)) {
            returnFlightSchedulePlan.setRecurrence(newFlightSchedulePlan.getRecurrence());
        }
        if(returnFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYDAY) ||
                returnFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYWEEK)) {
            returnFlightSchedulePlan.setEndDate(newFlightSchedulePlan.getEndDate());
        }
        newFlightSchedulePlan.setComplementaryReturnSchedulePlan(returnFlightSchedulePlan);
        returnFlightSchedulePlan.setComplementaryReturnSchedulePlan(newFlightSchedulePlan);
        createNewFlightSchedulePlan(returnFlightSchedulePlan);

        for (FlightSchedule flightSchedule : newFlightSchedulePlan.getFlightSchedules()) {
            FlightSchedule returnFlightSchedule = new FlightSchedule();
            Long returnDepartureTime = flightSchedule.getDepartureDateTime().getTime() + flightSchedule.getFlightDuration().getTime() + layoverDurationTime.getTime();
            returnFlightSchedule.setDepartureDateTime(new Date(returnDepartureTime));
            returnFlightSchedule.setFlightDuration(flightSchedule.getFlightDuration());
            returnFlightSchedule.setFlightSchedulePlan(returnFlightSchedulePlan);
            flightScheduleSessionBeanLocal.createNewFlightSchedule(returnFlightSchedule);
        }
        returnFlightSchedulePlan.setFirstDepartureTime(returnFlightSchedulePlan.getFirstDepartureTime());
    }

}
