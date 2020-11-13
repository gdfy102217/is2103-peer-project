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
import entity.Airport;
import entity.CabinClass;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.FlightRoute;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassType;
import util.enumeration.EmployeeType;
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
                        System.out.println("The New Aircraft Configuration Exceed MaximumSeat Capacity of the aircraft");
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
                    } catch (FlightRouteNotFoundException | DeleteFlightRouteException ex) {
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
        AircraftConfiguration newAircraftConfiguration = new AircraftConfiguration();

        System.out.println("*** FRSManagement :: Flight Planning Module :: Create New Aircraft Configuration ***\n");

        System.out.print("Enter Aircraft Configuration Name> ");
        newAircraftConfiguration.setAircraftConfigurationName(scanner.nextLine().trim());

        System.out.print("Enter Aircraft Type Name> ");
        AircraftType aircraftType = aircraftTypeSessionBeanRemote.retrieveAircraftTypeByName(scanner.nextLine().trim());
        newAircraftConfiguration.setAircraftType(aircraftType);

        Integer numOfCabinClasses = 0;
        while (numOfCabinClasses < 1 || numOfCabinClasses > 4) {
            System.out.print("Enter Number of Cabin Classes (1 to 4)> ");
            numOfCabinClasses = Integer.valueOf(scanner.nextLine().trim());
            newAircraftConfiguration.setNumOfCabinClasses(numOfCabinClasses);
        }
        
        aircraftConfigurationSessionBeanRemote.createNewAircraftConfiguration(newAircraftConfiguration);

        //to create each cabin class configuration
        Integer totalMaximumSeatCapacity = 0;
        for (int i = 0; i < numOfCabinClasses; i++) {
            CabinClass newCabinClass = new CabinClass();
            Integer cabinClassTypeSelection = 0;

            System.out.println("Select Cabin Class Type to be created> ");
            System.out.println("1: First Class");
            System.out.println("2: Business Class");
            System.out.println("3: Premium Economy Class");
            System.out.println("4: Economy Class");
            cabinClassTypeSelection = Integer.valueOf(scanner.nextLine().trim());
            switch (cabinClassTypeSelection) {
                case 1:
                    newCabinClass.setCabinClassType(CabinClassType.FIRSTCLASS);
                    break;
                case 2:
                    newCabinClass.setCabinClassType(CabinClassType.BUSINESSCLASS);
                    break;
                case 3:
                    newCabinClass.setCabinClassType(CabinClassType.PREMIUMECONOMYCLASS);
                    break;
                case 4:
                    newCabinClass.setCabinClassType(CabinClassType.ECONOMYCLASS);
                    break;
                default:
                    i--;
                    break;
            }
            
            CabinClassConfiguration newCabinClassConfiguration = new CabinClassConfiguration();
            newCabinClass.setCabinClassConfiguration(newCabinClassConfiguration);
            System.out.print("Enter Number of Aisles> ");
            newCabinClassConfiguration.setNumOfAisles(Integer.valueOf(scanner.nextLine().trim()));
            System.out.print("Enter Number of Rows> ");
            newCabinClassConfiguration.setNumOfRows(Integer.valueOf(scanner.nextLine().trim()));
            System.out.print("Enter Number of Seats Abreast> ");
            newCabinClassConfiguration.setNumOfSeatsAbreast(Integer.valueOf(scanner.nextLine().trim()));
            System.out.print("Enter Seating Configuration Per Column> ");
            newCabinClassConfiguration.setSeatingConfigurationPerColumn(scanner.nextLine().trim());

            System.out.println("The maximum seat capacity for this cabin class is " + newCabinClassConfiguration.getMaxSeatCapacity());
            totalMaximumSeatCapacity += newCabinClassConfiguration.getMaxSeatCapacity();
            System.out.println("The current maximum seat capacity for this aircraft configuration is " + totalMaximumSeatCapacity);
            if (totalMaximumSeatCapacity > newAircraftConfiguration.getAircraftType().getMaxPassengerSeatCapacity()) {
                throw new ExceedMaximumSeatCapacityException();
            }
            newCabinClass.setAircraftConfiguration(newAircraftConfiguration);
            cabinClassSessionBeanRemote.createNewCabinClass(newCabinClass, newCabinClassConfiguration);
        }
        
    }

    private void viewAllAircraftConfigurations() {
        System.out.println("*** FRSManagement :: Flight Planning Module :: View All Aircraft Configurations ***\n");
        for (AircraftConfiguration aircraftConfiguration : aircraftConfigurationSessionBeanRemote.retrieveAllAircraftConfigurations()) {
            System.out.println(aircraftConfiguration);
            
        }
    }

    private void viewAircraftConfigurationDetails() throws AircraftConfigurationNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRSManagement :: Flight Planning Module :: View Aircraft Configuration Details ***\n");
        System.out.print("Enter Aircraft Configuration Name> ");
        String nameOfAircraftConfiguration = scanner.nextLine().trim();
        AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(nameOfAircraftConfiguration);
        System.out.println(aircraftConfiguration);
        for (CabinClass cabinClass: aircraftConfiguration.getCabinClasses()) {
            System.out.println(cabinClass);
        }
    }

    private void doCreateNewFlightRoute() throws AirportNotFoundException, FlightRouteExistException, GeneralException {
        Scanner scanner = new Scanner(System.in);
        FlightRoute newFlightRoute = new FlightRoute();

        System.out.println("*** FRSManagement :: Flight Planning Module :: Create New Flight Route ***\n");

        System.out.print("Enter Origin Airport IATA code> ");
        Airport origin = airportSessionBeanRemote.retrieveAirportByIataCode(scanner.nextLine().trim());
        newFlightRoute.setOrigin(origin);

        System.out.print("Enter Destination Airport IATA code> ");
        Airport destination = airportSessionBeanRemote.retrieveAirportByIataCode(scanner.nextLine().trim());
        newFlightRoute.setDestination(destination);

        System.out.print("Create complementary flight? (Y/N)> ");
        String response = scanner.nextLine().trim();
        if (response == "Y") {
            FlightRoute newComplementaryFlightRoute = new FlightRoute();
            newComplementaryFlightRoute.setOrigin(destination);
            newComplementaryFlightRoute.setDestination(origin);
            newComplementaryFlightRoute.setComplementaryReturnRoute(newFlightRoute);
            newFlightRoute.setComplementaryReturnRoute(newComplementaryFlightRoute);
            flightRouteSessionBeanRemote.createNewFlightRoute(newComplementaryFlightRoute, destination, origin);
            System.out.println("Complementary flight route is created!");
        }
        flightRouteSessionBeanRemote.createNewFlightRoute(newFlightRoute, origin, destination);
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

    private void doDeleteFlightRoute() throws FlightRouteNotFoundException, DeleteFlightRouteException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Planning Module :: Delete Flight Route ***\n");

        System.out.print("Enter Origin Airport IATA code> ");
        String originCode = scanner.nextLine().trim();

        System.out.print("Enter Destination Airport IATA code> ");
        String destinationCode = scanner.nextLine().trim();

        FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOdPair(originCode, destinationCode);
        flightRouteSessionBeanRemote.deleteFlightRoute(flightRoute);
    }
}
