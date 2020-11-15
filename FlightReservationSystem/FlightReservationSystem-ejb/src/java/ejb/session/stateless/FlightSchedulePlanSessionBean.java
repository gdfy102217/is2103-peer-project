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
import java.util.ArrayList;
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
import util.exception.UpdateFlightSchedulePlanException;

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
    public FlightSchedulePlan createNewSingleFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight,
            Date departureDateTime, Date durationTime) throws FlightSchedulePlanExistException, GeneralException {
        try {
            em.merge(flight);
            em.flush();
            
            em.persist(newFlightSchedulePlan);
            newFlightSchedulePlan.setFlight(flight);
            flight.getFlightSchedulePlans().add(newFlightSchedulePlan);

            for (Fare fare : newFlightSchedulePlan.getFares()) {
                fare.setFlightSchedulePlan(newFlightSchedulePlan);
            }

            FlightSchedule newFlightSchedule = new FlightSchedule(departureDateTime, durationTime, flight.getFlightNumber(),
                    flight.getFlightRoute().getOrigin(), flight.getFlightRoute().getDestination(), newFlightSchedulePlan,
                    flight.getAircraftConfiguration().getCabinClasses());
            em.persist(newFlightSchedule);
            newFlightSchedule.setFlightSchedulePlan(newFlightSchedulePlan);
            newFlightSchedulePlan.getFlightSchedules().add(newFlightSchedule);

            em.flush();

            return newFlightSchedulePlan;
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
    public FlightSchedulePlan createNewMultipleFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight,
            List<Date> departureDateTimes, List<Date> durationTimes) throws FlightSchedulePlanExistException, GeneralException {
        try {
            em.merge(flight);
            em.persist(newFlightSchedulePlan);
            newFlightSchedulePlan.setFlight(flight);
            flight.getFlightSchedulePlans().add(newFlightSchedulePlan);

            for (Fare fare : newFlightSchedulePlan.getFares()) {
                fare.setFlightSchedulePlan(newFlightSchedulePlan);
            }

            for (int i = 0; i < departureDateTimes.size(); i++) {
                FlightSchedule newFlightSchedule = new FlightSchedule(departureDateTimes.get(i), durationTimes.get(i),
                        flight.getFlightNumber(), flight.getFlightRoute().getOrigin(), flight.getFlightRoute().getDestination(),
                        newFlightSchedulePlan, flight.getAircraftConfiguration().getCabinClasses());
                em.persist(newFlightSchedule);
                newFlightSchedule.setFlightSchedulePlan(newFlightSchedulePlan);
                newFlightSchedulePlan.getFlightSchedules().add(newFlightSchedule);
            }

            em.flush();

            return newFlightSchedulePlan;
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
    public FlightSchedulePlan createNewRecurrentFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight,
            Date departureDateTime, Date durationTime, Integer recurrence, Date endDateTime) throws FlightSchedulePlanExistException, GeneralException {
        try {
            em.merge(flight);
            em.persist(newFlightSchedulePlan);
            newFlightSchedulePlan.setFlight(flight);
            flight.getFlightSchedulePlans().add(newFlightSchedulePlan);

            for (Fare fare : newFlightSchedulePlan.getFares()) {
                fare.setFlightSchedulePlan(newFlightSchedulePlan);
            }

            while (departureDateTime.getTime() <= endDateTime.getTime()) {
                FlightSchedule newFlightSchedule = new FlightSchedule(departureDateTime, durationTime, flight.getFlightNumber(),
                        flight.getFlightRoute().getOrigin(), flight.getFlightRoute().getDestination(), newFlightSchedulePlan,
                        flight.getAircraftConfiguration().getCabinClasses());
                em.persist(newFlightSchedule);
                newFlightSchedule.setFlightSchedulePlan(newFlightSchedulePlan);
                newFlightSchedulePlan.getFlightSchedules().add(newFlightSchedule);

                departureDateTime = new Date(departureDateTime.getTime() + recurrence * 24 * 60 * 60 * 1000);
            }

            em.flush();
            return newFlightSchedulePlan;

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

        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();

        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
            flightSchedulePlan.getComplementaryReturnSchedulePlan();
            flightSchedulePlan.getFlight();
            flightSchedulePlan.getFares().size();
            flightSchedulePlan.getFlightScheduleType();
            flightSchedulePlan.getFlightSchedules().size();
        }
        return flightSchedulePlans;
    }

    @Override
    public List<FlightSchedulePlan> retrieveFlightSchedulePlansByFlightNumber(String flightNumber) throws FlightSchedulePlanNotFoundException {
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp WHERE fsp.flight.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);

        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();

        if (!flightSchedulePlans.isEmpty()) {
            for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
                flightSchedulePlan.getComplementaryReturnSchedulePlan();
                flightSchedulePlan.getFlight();
                flightSchedulePlan.getFares().size();
                flightSchedulePlan.getFlightScheduleType();
                flightSchedulePlan.getFlightSchedules().size();
            }
            return flightSchedulePlans;
        } else {
            throw new FlightSchedulePlanNotFoundException("Flight schedule plan with flight number " + flightNumber + " is not found!");
        }
    }

    @Override
    public void updateFlightSchedulePlanFares(FlightSchedulePlan flightSchedulePlan, List<Fare> fares) {
        em.merge(flightSchedulePlan);

        List<Fare> oldFares = flightSchedulePlan.getFares();

        flightSchedulePlan.setFares(fares);
        for (Fare fare : oldFares) {
            em.remove(fare);
        }
    }

    @Override
    public void updateSingleFlightSchedule(FlightSchedulePlan flightSchedulePlan, FlightSchedule flightSchedule, Date departureDateTime, Date durationTime) throws UpdateFlightSchedulePlanException {
        em.merge(flightSchedulePlan);
        em.merge(flightSchedule);

        if (!flightSchedule.getFlightReservations().isEmpty()) {
            throw new UpdateFlightSchedulePlanException("Flight schedule cannot be updated because tickets have already been issued!");
        } else {
            flightSchedulePlan.getFlightSchedules().remove(flightSchedule);

            Flight flight = flightSchedulePlan.getFlight();
            flightSchedulePlan.setFlight(flight);
            flight.getFlightSchedulePlans().add(flightSchedulePlan);

            FlightSchedule newFlightSchedule = new FlightSchedule(departureDateTime, durationTime, flight.getFlightNumber(),
                    flight.getFlightRoute().getOrigin(), flight.getFlightRoute().getDestination(), flightSchedulePlan,
                    flight.getAircraftConfiguration().getCabinClasses());
            em.persist(newFlightSchedule);
            newFlightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            flightSchedulePlan.getFlightSchedules().add(newFlightSchedule);
        }
    }

    @Override
    public void updateRecurrentDayFlightSchedule(FlightSchedulePlan flightSchedulePlan, Integer recurrence, Date endDate) throws UpdateFlightSchedulePlanException {
        em.merge(flightSchedulePlan);

        for (FlightSchedule flightSchedule : flightSchedulePlan.getFlightSchedules()) {
            if (!flightSchedule.getFlightReservations().isEmpty()) {
                throw new UpdateFlightSchedulePlanException("Flight schedule cannot be updated because tickets have already been issued!");
            }
        }
        flightSchedulePlan.setRecurrence(recurrence);
        flightSchedulePlan.setEndDate(endDate);

        FlightSchedule firstFlightSchedule = flightSchedulePlan.getFlightSchedules().get(0);
        Flight flight = flightSchedulePlan.getFlight();
        flightSchedulePlan.getFlightSchedules().clear();

        Date departureDateTime = firstFlightSchedule.getDepartureDateTime();
        while (departureDateTime.getTime() <= endDate.getTime()) {
            FlightSchedule newFlightSchedule = new FlightSchedule(departureDateTime, firstFlightSchedule.getFlightDuration(), flight.getFlightNumber(),
                    flight.getFlightRoute().getOrigin(), flight.getFlightRoute().getDestination(), flightSchedulePlan,
                    flight.getAircraftConfiguration().getCabinClasses());
            em.persist(newFlightSchedule);
            newFlightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            flightSchedulePlan.getFlightSchedules().add(newFlightSchedule);

            departureDateTime = new Date(departureDateTime.getTime() + recurrence * 24 * 60 * 60 * 1000);
        }

    }

    @Override
    public void updateRecurrentWeekFlightSchedule(FlightSchedulePlan flightSchedulePlan, Date endDate) throws UpdateFlightSchedulePlanException {
        em.merge(flightSchedulePlan);

        for (FlightSchedule flightSchedule : flightSchedulePlan.getFlightSchedules()) {
            if (flightSchedule.getDepartureDateTime().getTime() > endDate.getTime() && !flightSchedule.getFlightReservations().isEmpty()) {
                throw new UpdateFlightSchedulePlanException("Flight schedule cannot be updated because tickets have already been issued!");
            }
        }

        flightSchedulePlan.setEndDate(endDate);

        for (FlightSchedule flightSchedule : flightSchedulePlan.getFlightSchedules()) {
            if (flightSchedule.getDepartureDateTime().getTime() > endDate.getTime()) {
                flightSchedulePlan.getFlightSchedules().remove(flightSchedule);
            }
        }
    }

    @Override
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException {
        if (flightSchedulePlan.getFlightSchedules().isEmpty()) {
            flightSchedulePlan.getFlight().getFlightSchedulePlans().remove(flightSchedulePlan);
            FlightSchedulePlan complementaryFlightSchedulePlan = flightSchedulePlan.getComplementaryReturnSchedulePlan();
            complementaryFlightSchedulePlan.getFlight().getFlightSchedulePlans().remove(complementaryFlightSchedulePlan);
            em.remove(flightSchedulePlan);
            em.remove(complementaryFlightSchedulePlan);
        } else {
            flightSchedulePlan.setDisabled(true);
            throw new DeleteFlightSchedulePlanException("Flight Schedule Plan with Flight no. " + flightSchedulePlan.getFlight().getFlightNumber()
                    + " is in use and cannot be deleted! Instead, it is set as disabled. ");
        }
    }

    @Override
    public Long createReturnFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Flight flight, Date layoverDurationTime) throws FlightSchedulePlanExistException,
            GeneralException, FlightScheduleExistException {

        em.merge(newFlightSchedulePlan);
        em.merge(flight);
        newFlightSchedulePlan.setLayoverDuration(layoverDurationTime);

        List<Fare> fares = new ArrayList<>();
        fares.addAll(newFlightSchedulePlan.getFares());
        for (Fare fare : fares) {
            fare.setFlightSchedulePlan(newFlightSchedulePlan);
        }

        Date duration;
        Date departureDateTime;
        FlightSchedulePlan returnFlightSchedulePlan = new FlightSchedulePlan();

        if (newFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.SINGLE)) {
            duration = newFlightSchedulePlan.getFlightSchedules().get(0).getFlightDuration();
            departureDateTime = new Date(newFlightSchedulePlan.getFlightSchedules().get(0).getArrivalDateTime().getTime() + layoverDurationTime.getTime());

            returnFlightSchedulePlan = new FlightSchedulePlan(newFlightSchedulePlan.getFlightScheduleType(), fares, departureDateTime.getTime());
            createNewSingleFlightSchedulePlan(returnFlightSchedulePlan, flight.getComplementaryReturnFlight(), departureDateTime, duration);
        } else if (newFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.MULTIPLE)) {
            List<Date> durations = new ArrayList<>();
            List<Date> departureDateTimes = new ArrayList<>();
            Date firstDepartureTime = new Date();

            for (FlightSchedule flightSchedule : newFlightSchedulePlan.getFlightSchedules()) {
                duration = flightSchedule.getFlightDuration();
                departureDateTime = new Date(newFlightSchedulePlan.getFlightSchedules().get(0).getArrivalDateTime().getTime() + layoverDurationTime.getTime());

                if (departureDateTimes.isEmpty()) {
                    firstDepartureTime = departureDateTime;
                }
                durations.add(duration);
                departureDateTimes.add(departureDateTime);
            }

            returnFlightSchedulePlan = new FlightSchedulePlan(newFlightSchedulePlan.getFlightScheduleType(), fares, firstDepartureTime.getTime());
            createNewMultipleFlightSchedulePlan(returnFlightSchedulePlan, flight.getComplementaryReturnFlight(), departureDateTimes, durations);
        } else if (newFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYDAY)
                || newFlightSchedulePlan.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYWEEK)) {

            duration = newFlightSchedulePlan.getFlightSchedules().get(0).getFlightDuration();
            departureDateTime = new Date(newFlightSchedulePlan.getFlightSchedules().get(0).getArrivalDateTime().getTime() + layoverDurationTime.getTime());

            returnFlightSchedulePlan = new FlightSchedulePlan(newFlightSchedulePlan.getFlightScheduleType(), fares, departureDateTime.getTime());
            createNewRecurrentFlightSchedulePlan(newFlightSchedulePlan, flight, departureDateTime, duration, newFlightSchedulePlan.getRecurrence(),
                    new Date(departureDateTime.getTime() + newFlightSchedulePlan.getEndDate().getTime() - newFlightSchedulePlan.getFirstDepartureTime()));

        }

        returnFlightSchedulePlan.setComplementaryReturnSchedulePlan(newFlightSchedulePlan);
        newFlightSchedulePlan.setComplementaryReturnSchedulePlan(returnFlightSchedulePlan);

        em.flush();
        return returnFlightSchedulePlan.getFlightSchedulePlanId();
    }

}
