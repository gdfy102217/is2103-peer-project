/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClassType;
import util.enumeration.EmployeeType;
import util.exception.AircraftConfigurationExistExcetpion;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.ExceedMaximumSeatCapacityException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
public class FlightPlanningModule {

    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private CabinClassSessionBeanRemote cabinClassSessionBeanRemote;

    private Employee employee;

    public FlightPlanningModule() {
    }

    public FlightPlanningModule(AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote, 
            CabinClassSessionBeanRemote cabinClassSessionBeanRemote, Employee employee) {
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.cabinClassSessionBeanRemote = cabinClassSessionBeanRemote;
        this.employee = employee;
    }

    public void menuFlightPlanning() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management :: Flight Planning Module ***\n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configurations");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("-----------------------");
            System.out.println("4: Create Flight Route");
            System.out.println("5: View All Flight Routes");
            System.out.println("6: Delete Flight Route");
            System.out.println("-----------------------");
            System.out.println("7: Logout\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1 && employee.getEmployeeType().equals(EmployeeType.FLEETMANAGER)) {

                    try {
                        doCreateNewAircraftConfiguration();
                    } catch (ExceedMaximumSeatCapacityException ex) {
                        System.out.println("The New Aircraft Configuration Exceed Maximum Seat Capacity of the aircraft");
                    } catch (AircraftTypeNotFoundException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 2 && employee.getEmployeeType().equals(EmployeeType.FLEETMANAGER)) {
                    viewAllAircraftConfigurations();
                } else if (response == 3 && employee.getEmployeeType().equals(EmployeeType.FLEETMANAGER)) {
                    try {
                        viewAircraftConfigurationDetails();
                    } catch (AircraftConfigurationNotFoundException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 4 && employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER)) {
                    try {
                        doCreateNewFlightRoute();
                    } catch (AirportNotFoundException | FlightRouteExistException | GeneralException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 5 && employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER)) {
                    doViewAllFlightRoutes();
                } else if (response == 6 && employee.getEmployeeType().equals(EmployeeType.ROUTEPLANNER)) {
                    try {
                        doDeleteFlightRoute();
                    } catch (FlightRouteNotFoundException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 7) {
                    break;
                } else {
                    if (response > 0 || response < 7) {
                        System.out.println("You do not have permission!\n");
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
            }

            if (response == 7) {
                break;
            }
        }
    }

    private void doCreateNewAircraftConfiguration() throws ExceedMaximumSeatCapacityException, AircraftTypeNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Planning Module :: Create New Aircraft Configuration ***\n");

        System.out.print("Enter Aircraft Configuration Name> ");
        String aircraftConfigurationName = scanner.nextLine().trim();
        System.out.print("Enter Aircraft Type Name> ");
        String aircraftTypeName = scanner.nextLine().trim();
        System.out.print("Enter Aircraft Capacity> ");
        Integer aircraftCapacity = Integer.valueOf(scanner.nextLine().trim());
        
        AircraftType aircraftType = aircraftTypeSessionBeanRemote.retrieveAircraftTypeByName(aircraftTypeName);
        
        if (aircraftType.getMaxPassengerSeatCapacity() < aircraftCapacity) {
            System.out.println("Current configuration exceeds the maximum capacity of the " + aircraftType.getAircraftTypeName() + "!\n");
        } else {
            
            Integer numOfCabinClasses = 0;
            while (numOfCabinClasses < 1 || numOfCabinClasses > 4) {
                System.out.print("Enter Number of Cabin Classes (1 to 4)> ");
                numOfCabinClasses = Integer.valueOf(scanner.nextLine().trim());
            }
        
            AircraftConfiguration newAircraftConfiguration = new AircraftConfiguration(aircraftConfigurationName, numOfCabinClasses);

        //to create each cabin class configuration
        List<CabinClass> cabinClasses = new ArrayList<>();
        for (int i = 0; i < numOfCabinClasses; i++) {
            
            System.out.print("Select Cabin Class Code to be created> ");
            String cabinClassCode = scanner.nextLine().trim();
            System.out.print("Enter Number of Aisles> ");
            Integer numOfAisles = Integer.valueOf(scanner.nextLine().trim());
            System.out.print("Enter Number of Rows> ");
            Integer numOfRows = Integer.valueOf(scanner.nextLine().trim());
            System.out.print("Enter Number of Seats Abreast> ");
            Integer numOfSeatsAbreast = Integer.valueOf(scanner.nextLine().trim());
            System.out.print("Enter Seating Configuration Per Column> ");
            String seatingConfigurationPerColumn = scanner.nextLine().trim();
            System.out.print("Enter Capacity of the Cabin Class> ");
            Integer cabinClassCapacity = Integer.valueOf(scanner.nextLine().trim());
            
            CabinClassConfiguration cabinClassConfiguration = new CabinClassConfiguration(numOfAisles, numOfRows, numOfSeatsAbreast, seatingConfigurationPerColumn, cabinClassCapacity);
            
            if (cabinClassCode.charAt(0) == 'F') {
            cabinClasses.add(new CabinClass(CabinClassType.FIRSTCLASS, cabinClassConfiguration));
            } else if (cabinClassCode.charAt(0) == 'J') {
            cabinClasses.add(new CabinClass(CabinClassType.BUSINESSCLASS, cabinClassConfiguration));
            } else if (cabinClassCode.charAt(0) == 'W') {
            cabinClasses.add(new CabinClass(CabinClassType.PREMIUMECONOMYCLASS, cabinClassConfiguration));
            } else if (cabinClassCode.charAt(0) == 'Y') {
            cabinClasses.add(new CabinClass(CabinClassType.ECONOMYCLASS, cabinClassConfiguration));
            }
           }
            
            Long aircraftConfigurationId;
            try {
                aircraftConfigurationId = aircraftConfigurationSessionBeanRemote.createNewAircraftConfiguration(newAircraftConfiguration, aircraftType, cabinClasses);
                System.out.println("\nAircraft Confirguration With ID: " + aircraftConfigurationId + "is created successfully!\n");
            } catch (AircraftConfigurationExistExcetpion | GeneralException ex) {
                System.out.println("Error: " + ex.getMessage());
            } 
        }  
    }

    private void viewAllAircraftConfigurations() {
        System.out.println("*** FRSManagement :: Flight Planning Module :: View All Aircraft Configurations ***\n");
        List<AircraftConfiguration> aircraftConfigurations = aircraftConfigurationSessionBeanRemote.retrieveAllAircraftConfigurations();
        if (aircraftConfigurations.isEmpty()) {
            System.out.println("No Available Aircraft Configurations!\n");
        } else {
            for (AircraftConfiguration aircraftConfiguration : aircraftConfigurations) {
                System.out.println(aircraftConfiguration + "\n");
            }
        }
    }

    private void viewAircraftConfigurationDetails() throws AircraftConfigurationNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRSManagement :: Flight Planning Module :: View Aircraft Configuration Details ***\n");
        System.out.print("Enter Aircraft Configuration Name> ");
        String nameOfAircraftConfiguration = scanner.nextLine().trim();
        AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(nameOfAircraftConfiguration);
        System.out.println();
        System.out.println(aircraftConfiguration + "\n");
        for (CabinClass cabinClass: aircraftConfiguration.getCabinClasses()) {
            System.out.println("\t" + cabinClass);
        }
        System.out.println();
    }

    private void doCreateNewFlightRoute() throws AirportNotFoundException, FlightRouteExistException, GeneralException {
        Scanner scanner = new Scanner(System.in);
        FlightRoute newFlightRoute = new FlightRoute();

        System.out.println("*** FRSManagement :: Flight Planning Module :: Create New Flight Route ***\n");

        System.out.print("Enter Origin Airport IATA code> ");
        String originIataCode = scanner.nextLine().trim();

        System.out.print("Enter Destination Airport IATA code> ");
        String destinationIataCode = scanner.nextLine().trim();
        
        Long newFlightRouteId = flightRouteSessionBeanRemote.createNewFlightRoute(newFlightRoute, originIataCode, destinationIataCode);

        System.out.print("Create complementary flight? (Y/N)> ");
        String response = scanner.nextLine().trim();
        if ("Y".equals(response)) {
            FlightRoute newComplementaryFlightRoute = new FlightRoute();
            Long newComplementaryFlightRouteId = flightRouteSessionBeanRemote.createNewFlightRoute(newComplementaryFlightRoute, destinationIataCode, originIataCode);
            flightRouteSessionBeanRemote.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            System.out.println("Complementary flight route is created!");
        }       
        System.out.println("Flight route is created!");
    }

    private void doViewAllFlightRoutes() {
        System.out.println("*** FRSManagement :: Flight Planning Module :: View All Flight Routes ***\n");
        for (FlightRoute flightRoute : flightRouteSessionBeanRemote.retrieveAllFlightRoutes()) {
            System.out.println(flightRoute);
            if (flightRoute.getComplementaryReturnRoute() != null) {
                System.out.println("Complementary flight route [" + flightRoute.getComplementaryReturnRoute()+ "]");
            }
        }
    }

    private void doDeleteFlightRoute() throws FlightRouteNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Planning Module :: Delete Flight Route ***\n");

        System.out.print("Enter Origin Airport IATA code> ");
        String originCode = scanner.nextLine().trim();

        System.out.print("Enter Destination Airport IATA code> ");
        String destinationCode = scanner.nextLine().trim();

        FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOdPair(originCode, destinationCode);
        System.out.println("The flight route deleted is " + flightRoute);
        try {
            flightRouteSessionBeanRemote.deleteFlightRoute(flightRoute);
        } catch (DeleteFlightRouteException ex) {
            System.out.println("Flight route from " + flightRoute.getOrigin() + " to " + flightRoute.getDestination() + " is set disabled!");
        }
    }
}