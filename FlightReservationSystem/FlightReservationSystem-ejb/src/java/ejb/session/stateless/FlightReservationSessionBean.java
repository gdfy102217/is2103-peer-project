/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.Customer;
import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassType;
import util.exception.FlightReservationNotFoundException;
import util.exception.NoAvailableSeatsException;

/**
 *
 * @author xuyis
 */
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public FlightReservationSessionBean() {
    }

    public FlightReservation retrieveFlightReservationByID(Long flightReservationId) throws FlightReservationNotFoundException {
        FlightReservation flightReservation = em.find(FlightReservation.class, flightReservationId);

        if (flightReservation == null) {
            throw new FlightReservationNotFoundException("Flight Reservation with ID: " + flightReservationId + " does not exist!");
        }

        for (FlightSchedule flightSchedule : flightReservation.getFlightSchedules()) {
            flightSchedule.getDepartureAirport();
            flightSchedule.getDestinationAirport();
        }

        for (FlightSchedule returnFlightSchedule : flightReservation.getReturnFlightSchedules()) {
            returnFlightSchedule.getDepartureAirport();
            returnFlightSchedule.getDestinationAirport();
        }

        return flightReservation;
    }

    @Override
    public Long reserveFlight(Integer numOfPassengers, List<String[]> passengers, String[] creditCard, 
            List<Long> flightScheduleIds, List<Long> returnFlightScheduleIds, String departureAirportiATACode, 
            String destinationAirportiATACode, Date departureDate, Date returnDate, Customer customer) throws NoAvailableSeatsException {

        FlightReservation flightReservation = new FlightReservation(numOfPassengers, passengers, creditCard, departureAirportiATACode, destinationAirportiATACode, departureDate, returnDate, customer);
        em.persist(flightReservation);

        for (Long flightScheduleId : flightScheduleIds) {
            FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);

            for (String[] passenger : passengers) {
                CabinClassType cabinClassType;
                if (passenger[3].charAt(0) == '1') {
                    cabinClassType = CabinClassType.FIRSTCLASS;
                } else if (passenger[3].charAt(0) == '2') {
                    cabinClassType = CabinClassType.BUSINESSCLASS;
                } else if (passenger[3].charAt(0) == '3') {
                    cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                } else {
                    cabinClassType = CabinClassType.ECONOMYCLASS;
                }

                for (CabinClass cabinClass : flightSchedule.getCabinClasses()) {
                    if (cabinClass.equals(cabinClassType)) {
                        if (cabinClass.getNumOfBalanceSeats() == 0) {
                            throw new NoAvailableSeatsException("The chosen cabin class does not have enough seats for the reservation, please choose another one!");
                        } else {
                            cabinClass.setNumOfReservedSeats(cabinClass.getNumOfReservedSeats() + 1);
                        }
                    }
                }
            }
            flightSchedule.getFlightReservations().add(flightReservation);
            flightReservation.getFlightSchedules().add(flightSchedule);
        }

        if (!returnFlightScheduleIds.isEmpty()) {
            for (Long returnFlightScheduleId : returnFlightScheduleIds) {
                FlightSchedule returnFlightSchedule = em.find(FlightSchedule.class, returnFlightScheduleId);

                for (String[] passenger : passengers) {
                    CabinClassType cabinClassType;
                    if (passenger[3].charAt(0) == '1') {
                        cabinClassType = CabinClassType.FIRSTCLASS;
                    } else if (passenger[3].charAt(0) == '2') {
                        cabinClassType = CabinClassType.BUSINESSCLASS;
                    } else if (passenger[3].charAt(0) == '3') {
                        cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                    } else {
                        cabinClassType = CabinClassType.ECONOMYCLASS;
                    }

                    for (CabinClass cabinClass : returnFlightSchedule.getCabinClasses()) {
                        if (cabinClass.equals(cabinClassType)) {
                            if (cabinClass.getNumOfBalanceSeats() == 0) {
                                throw new NoAvailableSeatsException("The chosen cabin class does not have enough seats for the reservation, please choose another one!");
                            } else {
                                cabinClass.setNumOfReservedSeats(cabinClass.getNumOfReservedSeats() + 1);
                            }
                        }
                    }
                }
                returnFlightSchedule.getFlightReservations().add(flightReservation);
                flightReservation.getReturnFlightSchedules().add(returnFlightSchedule);
            }
        }
        
        em.flush();
        
        return flightReservation.getFlightReservationId();
    }

    
}
