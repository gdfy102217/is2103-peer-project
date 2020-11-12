/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.CabinClass;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.CabinClassType;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightScheduleException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightScheduleNotFountException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException {
        try {
            em.persist(flightSchedule);
            flightSchedule.getFlightSchedulePlan().getFlightSchedules().add(flightSchedule);
            em.flush();

            return flightSchedule;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new FlightScheduleExistException("This flight schedule already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<FlightSchedule> searchFlightScehdules(String departureAirportName, String destinationAirportName, Date departureDate, Integer numOfPassengers, Integer flightTypePreference, CabinClassType cabinClassType) throws AirportNotFoundException, FlightScheduleNotFountException {

        Airport departureAirport = airportSessionBeanLocal.retrieveAirportByName(departureAirportName);
        Airport destinationAirport = airportSessionBeanLocal.retrieveAirportByName(destinationAirportName);

        if (flightTypePreference == 1) { //direct flight
            Query query = em.createQuery("SELECT f FROM FlightSchedule f WHERE f.departureDateTime =: departureDate JOIN f.flightSchedulePlan p JOIN p.flight t JOIN t.flightRoute r WHERE r.origin = : departureAirport AND r.destination =: destinationAirport");
            query.setParameter("departureDate", departureDate);
            query.setParameter("departureAirport", departureAirport);
            query.setParameter("destinationAirport", destinationAirport);

            List<FlightSchedule> flightSchedules = query.getResultList();

            if (flightSchedules.isEmpty()) {
                throw new FlightScheduleNotFountException("Flight schedule departure from: " + departureAirport.getIataAirportcode() + " to " + departureAirport + " on date: " + departureDate + " does not exist!");
            }

            List<FlightSchedule> availableFlightSchedules = new ArrayList<>();

            for (FlightSchedule flightSchedule : flightSchedules) {
                for (CabinClass c : flightSchedule.getCabinClasses()) {
                    if (c.getCabinClassType().equals(cabinClassType)) {
                        if (c.getNumOfBalanceSeats() >= numOfPassengers) {
                            availableFlightSchedules.add(flightSchedule);
                            flightSchedule.getDepartureAirport();
                            flightSchedule.getDestinationAirport();
                            flightSchedule.getFlightSchedulePlan();
                        }
                    }
                }

            }

            return availableFlightSchedules;

        } else if (flightTypePreference == 2) { //connecting flight
        }
    }
              
    @Override
    public void viewSeatsInventory(FlightSchedule flightSchedule) {
        for (CabinClass cabinClass: flightSchedule.getCabinClasses()) {
            System.out.println("Cabin class type: " + cabinClass.getCabinClassConfiguration().getCabinClassType());
            System.out.println("No. of seats available: " + cabinClass.getCabinClassConfiguration().getMaxSeatCapacity());
            System.out.println("No. of seats reserved: " + cabinClass.getNumOfReservedSeats());
            System.out.println("No. of balance seats: " + (cabinClass.getCabinClassConfiguration().getMaxSeatCapacity() - cabinClass.getNumOfReservedSeats()));
        }
    }
    
    @Override
    public void viewFlightReservation(FlightSchedule flightSchedule) {
        for(CabinClass cabinClass: flightSchedule.getCabinClasses()) {
            System.out.println(cabinClass.getCabinClassConfiguration().getCabinClassType() + " reservation list:");
            for(FlightReservation reservation: flightSchedule.getFlightReservations()) {
                if (reservation.getCabinClassType().equals(cabinClass.getCabinClassConfiguration().getCabinClassType())) {
                    for(String[] passengerInfo: reservation.getPassengers()) {
                        System.out.println("firstName " + passengerInfo[0] + ", lastName " + passengerInfo[1] + ", passportNumber " + passengerInfo[2] + ", seatNumber " + passengerInfo[3]);
                    }
                }
            }
        }
    }
    
    @Override
    public void deleteFlightSchedule(FlightSchedule flightSchedule) throws DeleteFlightScheduleException {
        if(flightSchedule.getFlightReservations().isEmpty() == false) {
            
            throw new DeleteFlightScheduleException("This flight schedule has reservation!");
        }
        
        flightSchedule.getFlightSchedulePlan().getFlightSchedules().remove(flightSchedule);
        flightSchedule.setFlightSchedulePlan(new FlightSchedulePlan());
        em.remove(flightSchedule);
        System.out.println("This flight schedule has been successfully deleted!");
    }
}
