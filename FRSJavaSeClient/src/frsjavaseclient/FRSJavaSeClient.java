/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsjavaseclient;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ws.client.partner.CustomerNotFoundException_Exception;
import ws.client.partner.GeneralException_Exception;
import ws.client.partner.Partner;
import ws.client.reservation.FlightReservation;
import ws.client.reservation.FlightReservationNotFoundException_Exception;

/**
 *
 * @author Administrator
 */
public class FRSJavaSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name of partner> ");
        String username = scanner.nextLine().trim();
        try {
            Partner currentPartner = partnerLogin(username);
        } catch (CustomerNotFoundException_Exception | GeneralException_Exception ex) {
            System.out.println(ex);
        }

        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management :: Flight Planning Module ***\n");
            System.out.println("1: Reserve Flight");
            System.out.println("2: Retrieve Flight Reservation By ID");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    System.out.print("Enter number of passengers> ");
                    Integer numOfPassengers = Integer.valueOf(scanner.nextLine().trim());
                    
                    List<String[]> passengerList;
                    List<String> creditCardList;
                    for (int i = 0; i < numOfPassengers; i++) 
                    {
                        System.out.print("Enter passenger particulars> ");
                        String passenger = scanner.nextLine().trim();
                        String[] passengerParticulars = passenger.split(",");
                        passengerList.add(passengerParticulars);
                        
                        System.out.print("Enter credit card number> ");
                        String cardNumber = scanner.nextLine().trim();
                        creditCardList.add(cardNumber);
                        
                        System.out.print("Enter credit card number> ");
                        String cabinClassType = scanner.nextLine().trim();
                    }
                    
                    System.out.print("Enter number of passengers> ");
                    Integer numOfPassengers = Integer.valueOf(scanner.nextLine().trim());
                    
                    System.out.print("Enter number of passengers> ");
                    Integer numOfPassengers = Integer.valueOf(scanner.nextLine().trim());
                    
                    System.out.print("Enter number of passengers> ");
                    Integer numOfPassengers = Integer.valueOf(scanner.nextLine().trim());
                    
                } else if (response == 2) {
                    System.out.print("Enter flight reservation ID> ");
                    Long reservationId = Long.valueOf(scanner.nextLine().trim());
                    try {
                        FlightReservation flightReservation = retrieveFlightReservationByID(reservationId);
                        System.out.println(flightReservation);
                    } catch (FlightReservationNotFoundException_Exception ex) {
                        System.out.println(ex);
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private static Partner partnerLogin(java.lang.String username) throws CustomerNotFoundException_Exception, GeneralException_Exception {
        ws.client.partner.PartnerEntityWebService_Service service = new ws.client.partner.PartnerEntityWebService_Service();
        ws.client.partner.PartnerEntityWebService port = service.getPartnerEntityWebServicePort();
        return port.partnerLogin(username);
    }

    private static FlightReservation retrieveFlightReservationByID(java.lang.Long flightReservationId) throws FlightReservationNotFoundException_Exception {
        ws.client.reservation.FlightReservationWebService_Service service = new ws.client.reservation.FlightReservationWebService_Service();
        ws.client.reservation.FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.retrieveFlightReservationByID(flightReservationId);
    }

    private static Long reserveFlight(java.lang.Integer numOfPassengers, java.util.List<ws.client.reservation.StringArray> passengers, 
            java.util.List<java.lang.String> creditCard, ws.client.reservation.CabinClassType cabinClassType, java.util.List<java.lang.Long> flightScheduleIds, 
            java.util.List<java.lang.Long> returnFlightScheduleIds, ws.client.reservation.Customer customer) {
        ws.client.reservation.FlightReservationWebService_Service service = new ws.client.reservation.FlightReservationWebService_Service();
        ws.client.reservation.FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.reserveFlight(numOfPassengers, passengers, creditCard, cabinClassType, flightScheduleIds, returnFlightScheduleIds, customer);
    }
    
    
}
