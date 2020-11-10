/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import util.enumeration.EmployeeType;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
public class FlightOperationModule {
    
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    
    private Employee employee;

    public FlightOperationModule() {
    }
    
    public void menuFlightOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** FRS Management :: Flight Operation Module ***\n");
            System.out.println("1: Create Flight ");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("-----------------------");
            System.out.println("4: Create Flight Schedule Plan");
            System.out.println("5: View All Flight Routes");
            System.out.println("6: Delete Flight Route");
            System.out.println("-----------------------");
            System.out.println("7: Back\n");
            response = 0;
            
            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                    try {
                        doCreateNewFlight();
                    } catch (FlightRouteNotFoundException | AircraftConfigurationNotFoundException | FlightExistException | GeneralException ex) {
                        System.out.println(ex);
                    }
                }
                else if(response == 2)
                {
                    viewAllFlights();
                }
                else if(response == 3)
                {
                    try {
                        viewFlightDetails();
                    } catch (FlightNotFoundException | DeleteFlightException ex) {
                        System.out.println(ex);
                    }
                }
                else if(response == 4)
                {
                    try {
                        doCreateNewFlightSchedulePlan();
                    } catch () {
                        System.out.println(ex);
                    }
                }
                else if(response == 5 && employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER))
                {
                    doViewAllFlightRoutes();
                }
                else if(response == 6 && employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER))
                {
                    try {
                        doDeleteFlightRoute();
                    } catch (FlightRouteNotFoundException | DeleteFlightRouteException ex) {
                        System.out.println(ex);
                    }
                }
                else if (response == 7)
                {
                    break;
                }
                else
                {
                    if (response > 0 || response < 7) {
                        System.out.println("You do not have permission!\n");
                    } else {
                        System.out.println("Invalid option, please try again!\n");   
                    }
                }
            }
            
            if(response == 7)
            {
                break;
            }
        }
    }
    
    private void doCreateNewFlight() throws FlightRouteNotFoundException, AircraftConfigurationNotFoundException, FlightExistException, GeneralException {
        Scanner scanner = new Scanner(System.in);
        Flight newFlight = new Flight();
        
        System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Flight ***\n");
        
        System.out.print("Enter Flight Number> ");
        newFlight.setFlightNumber(scanner.nextLine().trim());
        
        System.out.print("Enter Origin Airport IATA code> ");
        String originCode = scanner.nextLine().trim();
        
        System.out.print("Enter Destination Airport IATA code> ");
        String destinationCode = scanner.nextLine().trim();
        
        FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOdPair(originCode, destinationCode);
        newFlight.setFlightRoute(flightRoute);
        
        System.out.print("Enter Aircraft Configuration Name> ");
        AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(scanner.nextLine().trim());
        newFlight.setAircraftConfiguration(aircraftConfiguration);
        
        if (newFlight.getFlightRoute().getComplementaryReturnRoute() != null) {
            System.out.print("Create a complementary return flight? (Y/N)> ");
            if (scanner.nextLine().trim().equals("Y")) {
                Flight newComplementaryFlight = new Flight();
                System.out.print("Enter Complementary Flight Number> ");
                newFlight.setFlightNumber(scanner.nextLine().trim());
                newComplementaryFlight.setFlightRoute(flightRoute.getComplementaryReturnRoute());
                newComplementaryFlight.setAircraftConfiguration(aircraftConfiguration);
                newComplementaryFlight.setComplementaryReturnFlight(newFlight);
                flightSessionBeanRemote.createNewFlight(newComplementaryFlight);
            }
        }
        flightSessionBeanRemote.createNewFlight(newFlight);
    }
    
    private void viewAllFlights() {
        System.out.println("*** FRSManagement :: Flight Operation Module :: View All Flights ***\n");
        for (Flight flight: flightSessionBeanRemote.retrieveAllFlights()) {
            System.out.println(flight);
            if (flight.getComplementaryReturnFlight() != null) {
                System.out.println("Complementary Flight = " + flight.getComplementaryReturnFlight());
            }
        }
    }
    
    private void viewFlightDetails() throws FlightNotFoundException, DeleteFlightException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRSManagement :: Flight Operation Module :: View Flight Details ***\n");
        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        System.out.println(flight);
        
        System.out.print("Update details of this flight? (Y/N)> ");
        if (scanner.nextLine().trim().equals("Y")) {
            System.out.print("Enter field name> ");
            String fieldName = scanner.nextLine().trim();
        }
        
        System.out.print("Delete this flight? (Y/N)> ");
        if (scanner.nextLine().trim().equals("Y")) {
            flightSessionBeanRemote.deleteFlight(flight);
        }
    }
    
    private void doCreateNewFlightSchedulePlan() throws FlightNotFoundException, ParseException {
        Scanner scanner = new Scanner(System.in);
        FlightSchedulePlan newFlightSchedulePlan = new FlightSchedulePlan();
        System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Flight Schedule Plan ***\n");
        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        newFlightSchedulePlan.setFlight(flight);
        
        while(true) {
            System.out.println("*** Create Flight Schedule ***\n");
            System.out.print("Enter Departure Date/Time> ");
            String departureDateTimeString = scanner.nextLine().trim();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date departureDateTime = format.parse(departureDateTimeString);
            
        }
    }
}
