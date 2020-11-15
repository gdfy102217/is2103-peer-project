/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.FlightScheduleType;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.DeleteFlightScheduleException;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FareExistException;
import util.exception.FlightDisabledException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteDisabledException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
public class FlightOperationModule {

    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;

    private Employee employee;

    public FlightOperationModule() {
    }

    public FlightOperationModule(FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote, Employee employee) {
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
        this.employee = employee;
    }

    public void menuFlightOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management :: Flight Operation Module ***\n");
            System.out.println("1: Create Flight ");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("-----------------------");
            System.out.println("4: Create Flight Schedule Plan");
            System.out.println("5: View All Flight Schedule Plans");
            System.out.println("6: View Flight Schedule Plan Details");
            System.out.println("-----------------------");
            System.out.println("7: Logout\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewFlight();
                } else if (response == 2) {
                    viewAllFlights();
                } else if (response == 3) {
                    viewFlightDetails();
                } else if (response == 4) {
                    doCreateNewFlightSchedulePlan();
                } else if (response == 5) {
                    doViewAllFlightSchedulePlans();
                } else if (response == 6) {
                    try {
                        viewFlightSchedulePlanDetails();
                    } catch (DeleteFlightScheduleException | ParseException | FlightScheduleExistException | GeneralException |
                            FareExistException | FlightSchedulePlanNotFoundException | DeleteFlightSchedulePlanException | FlightNotFoundException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
            }
        }
    }

    private void doCreateNewFlight(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Flight ***\n");
            
            System.out.print("Enter Flight Number> ");
            String flightNumber = scanner.nextLine().trim();
            Flight newFlight = new Flight(flightNumber);
            
            System.out.print("Enter Origin Airport IATA code> ");
            String originCode = scanner.nextLine().trim();
            
            System.out.print("Enter Destination Airport IATA code> ");
            String destinationCode = scanner.nextLine().trim();
            
            System.out.print("Enter Aircraft Configuration Name> ");
            String aircraftConfigurationName = scanner.nextLine().trim();
            
            Long newFlightId = flightSessionBeanRemote.createNewFlight(newFlight, originCode, destinationCode, aircraftConfigurationName);
            System.out.println("Flight with ID: " + newFlightId + " is created!\n");
            
            if (flightRouteSessionBeanRemote.checkIfComplementaryFlightRouteExist(originCode, destinationCode)) {
                System.out.print("Create a complementary return flight? (Y/N)> ");
                String createOption = scanner.nextLine().trim();
                if (createOption.equals("Y")) {
                    
                    System.out.print("Enter Complementary Flight Number> ");
                    String complementaryFlightNumber = scanner.nextLine().trim();
                    Flight newComplementaryFlight = new Flight(complementaryFlightNumber);
                    
                    Long newComplementaryReturnFlightId = flightSessionBeanRemote.createNewFlight(newComplementaryFlight, destinationCode, originCode, aircraftConfigurationName);
                    flightSessionBeanRemote.associateComplementaryFlight(newFlightId, newComplementaryReturnFlightId);
                    System.out.println("Complementary Flight with ID: " + newComplementaryReturnFlightId + " is created!\n");
                }
            }
        } catch (FlightExistException | GeneralException | FlightRouteNotFoundException | AircraftConfigurationNotFoundException | FlightRouteDisabledException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void viewAllFlights() {
        System.out.println("*** FRSManagement :: Flight Operation Module :: View All Flights ***\n");
        for (Flight flight : flightSessionBeanRemote.retrieveAllFlights()) {
            System.out.println(flight);
            if (flight.getComplementaryReturnFlight() != null) {
                System.out.println("Complementary Flight = [" + flight.getComplementaryReturnFlight() + "]");
            }
        }
        System.out.println();
    }

    private void viewFlightDetails() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** FRSManagement :: Flight Operation Module :: View Flight Details ***\n");
            System.out.print("Enter Flight Number> ");
            String flightNumber = scanner.nextLine().trim();
            
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            
            System.out.println(flight);
            System.out.println(flight.getFlightRoute());
            for (CabinClass cabinClass: flight.getAircraftConfiguration().getCabinClasses()) {
                System.out.println(cabinClass.getCabinClassType());
                System.out.println("Available seats = " + cabinClass.getCabinClassConfiguration().getCabinClassCapacity());
            }
            System.out.println();
            
            System.out.print("Update details of this flight? (Y/N)> ");
            if (scanner.nextLine().trim().equals("Y")) {
                updateFlight(flight);
            }
            
            System.out.print("Delete this flight? (Y/N)> ");
            if (scanner.nextLine().trim().equals("Y")) {
                flightSessionBeanRemote.deleteFlight(flight);
            }
        } catch (FlightNotFoundException | DeleteFlightException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    private void updateFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Operation Module :: Update Flight ***\n");
        System.out.print("Enter New Aircraft Configuration Name> ");
        String newAircraftConfigurationName = scanner.nextLine().trim();
        
        try {
            flightSessionBeanRemote.updateFlight(flight, newAircraftConfigurationName);
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    private void doCreateNewFlightSchedulePlan() {
        try {
            Scanner scanner = new Scanner(System.in);    
            System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Flight Schedule Plan ***\n");
            System.out.print("Enter Flight Number> ");
            String flightNumber = scanner.nextLine().trim();
            
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            if (flight.getDisabled() == true) {
                System.out.println("This flight is disabled, not able to create a schedule plan!");
            }
            
            
            System.out.println("Select Type of Flight Schedule Plan> ");
            System.out.println("1: Single");
            System.out.println("2: Multiple");
            System.out.println("3: Recurrent schedule every n day");
            System.out.println("4: Recurrent schedule every week");
            System.out.print("> ");
            Integer response = Integer.valueOf(scanner.nextLine().trim());
            
            switch (response) {
                case (1):
                    FlightSchedulePlan newFlightSchedulePlan = new FlightSchedulePlan(FlightScheduleType.SINGLE, flight); //rmb to merge flight
                    
                    System.out.println("*** Create Single Flight Schedule ***\n");
                    System.out.print("Enter Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                    String departureDateTimeString = scanner.nextLine().trim();
                    DateFormat format = new SimpleDateFormat("year-month-day hr:min");
                    Date departureDateTime = format.parse(departureDateTimeString);

                    System.out.print("Enter Flight Duration (hr:min)> ");
                    String durationString = scanner.nextLine().trim();
                    DateFormat durationFormat = new SimpleDateFormat("hr:min");
                    Date durationTime = durationFormat.parse(durationString);
                    
                    flightScheduleSessionBeanRemote.createNewSingleFlightSchedule(flight, newFlightSchedulePlan, departureDateTime, durationTime);
                    break;
                case (2):
                    newFlightSchedulePlan.setFlightScheduleType(FlightScheduleType.MULTIPLE);
                    break;
                case (3):
                    newFlightSchedulePlan.setFlightScheduleType(FlightScheduleType.RECURRENTBYDAY);
                    System.out.print("Recurrence by how many days> ");
                    Integer recurrence = Integer.valueOf(scanner.nextLine().trim());
                    newFlightSchedulePlan.setRecurrence(recurrence);
                    break;
                case (4):
                    newFlightSchedulePlan.setFlightScheduleType(FlightScheduleType.RECURRENTBYWEEK);
                    break;
                default:
            }
            
            if (response == 3 || response == 4) {
                System.out.print("Enter End Date (yyyy-mm-dd)> ");
                String endDateString = scanner.nextLine().trim();
                DateFormat format = new SimpleDateFormat("year-month-day");
                Date endDateTime = format.parse(endDateString);
                newFlightSchedulePlan.setEndDate(endDateTime);
            }
            
            flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(newFlightSchedulePlan);
            
            while (true) {
                
                
                if (flightScheduleSessionBeanRemote.checkOverlapFlightSchedules(newFlightSchedule)) {
                    newFlightSchedule.setFlightSchedulePlan(newFlightSchedulePlan);
                    flightScheduleSessionBeanRemote.createNewFlightSchedule(newFlightSchedule);
                } else {
                    System.out.println("New flight schedule overlap with existing flight schedules!");
                }
                
                if (!newFlightSchedulePlan.getFlightSchedules().isEmpty()) {
                    System.out.print("Continue to create more flight schedule? (Y/N)> ");
                    if (scanner.nextLine().trim().equals("N")) {
                        newFlightSchedulePlan.setFirstDepartureTime(newFlightSchedulePlan.getFirstDepartureTime());
                        break;
                    }
                }
            }
            
            if (flight.getComplementaryReturnFlight() != null) {
                System.out.print("Create a complementary return flight schedule plan? (Y/N)> ");
                
                if (scanner.nextLine().trim().equals("Y")) {
                    System.out.print("Enter Layover Duration (hr:min)> ");
                    String layoverDurationString = scanner.nextLine().trim();
                    DateFormat durationFormat = new SimpleDateFormat("hr:min");
                    Date layoverDurationTime = durationFormat.parse(layoverDurationString);
                    
                    flightSchedulePlanSessionBeanRemote.createReturnFlightSchedulePlan(newFlightSchedulePlan, layoverDurationTime);
                }
            }
            
            doCreateFare(newFlightSchedulePlan);
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doCreateFare(FlightSchedulePlan newFlightSchedulePlan) throws FareExistException, GeneralException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Fares ***\n");
        List<CabinClass> cabinClasses = newFlightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClasses();
        for (CabinClass cabinClass : cabinClasses) {
            while (true) {
                Fare newFare = new Fare();
                newFare.setCabinClassType(cabinClass.getCabinClassType());
                newFare.setFlightSchedulePlan(newFlightSchedulePlan);

                System.out.print("Enter Fare Basis Code> ");
                String fareBasisCode = scanner.nextLine().trim();
                newFare.setFareBasisCode(fareBasisCode);

                System.out.print("Enter Fare Amount for " + newFare.getCabinClassType() + "> ");
                String fareAmount = scanner.nextLine().trim();
                newFare.setFareAmount(Double.valueOf(fareAmount));

                fareSessionBeanRemote.createNewFare(newFare);
                System.out.print("Continue to create more fare for " + newFare.getCabinClassType() + " ? (Y/N)> ");
                if (scanner.nextLine().trim().equals("N")) {
                    break;
                }
            }
        }
    }

    private void doViewAllFlightSchedulePlans() {
        System.out.println("*** FRSManagement :: Flight Operation Module :: View All Flight Schedule Plans ***\n");

        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlanSessionBeanRemote.retrieveAllFlightSchedulePlans()) {
            System.out.println(flightSchedulePlan);
            if (flightSchedulePlan.getComplementaryReturnSchedulePlan() != null) {
                System.out.println("Complementary return schedule plan [ " + flightSchedulePlan.getComplementaryReturnSchedulePlan() + " ]");
            }
        }
    }

    private void viewFlightSchedulePlanDetails() throws DeleteFlightScheduleException, ParseException, FlightScheduleExistException,
            GeneralException, FareExistException, FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException, FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Operation Module :: View Flight Schedule Plan Details ***\n");
        System.out.print("Enter Flight Number of the Flight Schedule Plan> ");
        String flightNumber = scanner.nextLine().trim();
        List<FlightSchedulePlan> list = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlansByFlightNumber(flightNumber);
        Integer selection = 0;
        for (FlightSchedulePlan flightSchedulePlan : list) {
            System.out.println("No." + selection + " " + flightSchedulePlan);
            selection++;
        }
        System.out.print("Enter no. to view details> ");
        Integer userSelection = Integer.valueOf(scanner.nextLine().trim());
        FlightSchedulePlan flightSchedulePlanSelected = list.get(userSelection);

        System.out.println("Flight O-D pair=[" + flightSchedulePlanSelected.getFlight() + "]");
        System.out.println("Flight schedule: ");
        for (FlightSchedule flightSchedule : flightSchedulePlanSelected.getFlightSchedules()) {
            System.out.println(flightSchedule);
        }
        System.out.println("Fare: ");
        for (Fare fare : flightSchedulePlanSelected.getFares()) {
            System.out.println(fare);
        }

        System.out.print("Update details of this flight schedule plan? (Y/N)> ");
        if (scanner.nextLine().trim().equals("Y")) {
            updateFlightSchedulePlan(flightSchedulePlanSelected);
        }

        System.out.print("Delete this flight schedule plan? (Y/N)> ");
        if (scanner.nextLine().trim().equals("Y")) {
            flightSchedulePlanSessionBeanRemote.deleteFlightSchedulePlan(flightSchedulePlanSelected);
        }
    }

    private void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlanSelected) throws DeleteFlightScheduleException, ParseException,
            FlightScheduleExistException, GeneralException, FareExistException, FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Operation Module :: Update Flight Schedule Plan ***\n");
        System.out.println("1: Update Flight ");
        System.out.println("2: Delete Existing Flight Schedule");
        System.out.println("3: Create New Flight Schedule");
        System.out.println("4: Delete Fare");
        System.out.println("5: Create New Fare");
        System.out.println("6: Update Flight Schedule Type");
        System.out.println("7: Update Layover Duration");
        System.out.print("> ");

        Integer response = scanner.nextInt();
        scanner.nextLine();

        if (response == 1) {
            System.out.print("Enter New Flight Number> ");
            String flightNumber = scanner.nextLine().trim();
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            flightSchedulePlanSelected.setFlight(flight);
        } else if (response == 2) {
            Integer selection = 0;
            System.out.println("Flight schedule: ");
            for (FlightSchedule flightSchedule : flightSchedulePlanSelected.getFlightSchedules()) {
                System.out.println("No." + selection + ": " + flightSchedule);
            }
            System.out.print("> ");
            selection = Integer.valueOf(scanner.nextLine().trim());
            FlightSchedule flightScheduleSelected = flightSchedulePlanSelected.getFlightSchedules().get(selection);

            System.out.println("The flight schedule selected is " + flightScheduleSelected);

            Integer flightScheduleResponse = Integer.valueOf(scanner.nextLine().trim());

            flightScheduleSessionBeanRemote.deleteFlightSchedule(flightScheduleSelected);

        } else if (response == 3) {
            FlightSchedule newFlightSchedule = new FlightSchedule();
            System.out.print("Enter Departure Date/Time (yyyy-mm-dd hh:mm)> ");
            String departureDateTimeString = scanner.nextLine().trim();
            DateFormat format = new SimpleDateFormat("year-month-day hr:min");
            Date departureDateTime = format.parse(departureDateTimeString);
            newFlightSchedule.setDepartureDateTime(departureDateTime);

            System.out.print("Enter Flight Duration (hr:min)> ");
            String durationString = scanner.nextLine().trim();
            DateFormat durationFormat = new SimpleDateFormat("hr:min");
            Date durationTime = durationFormat.parse(durationString);
            newFlightSchedule.setFlightDuration(durationTime);

            newFlightSchedule.setFlightSchedulePlan(flightSchedulePlanSelected);
            flightScheduleSessionBeanRemote.createNewFlightSchedule(newFlightSchedule);
        } else if (response == 4) {
            Integer selection = 0;
            System.out.println("Fare list: ");
            for (Fare fare : flightSchedulePlanSelected.getFares()) {
                System.out.println("No." + selection + ": " + fare);
            }
            System.out.print("> ");
            selection = Integer.valueOf(scanner.nextLine().trim());
            Fare fareSelected = flightSchedulePlanSelected.getFares().get(selection);

            fareSessionBeanRemote.deleteFare(fareSelected);
        } else if (response == 5) {
            doCreateFare(flightSchedulePlanSelected);
        } else if (response == 6) {
            System.out.println("Select Type of Flight Schedule Plan> ");
            System.out.println("1: Single");
            System.out.println("2: Multiple");
            System.out.println("3: Recurrent schedule every n day");
            System.out.println("4: Recurrent schedule every week");
            System.out.print("> ");
            Integer option = Integer.valueOf(scanner.nextLine().trim());

            switch (option) {
                case (1):
                    flightSchedulePlanSelected.setFlightScheduleType(FlightScheduleType.SINGLE);
                    break;
                case (2):
                    flightSchedulePlanSelected.setFlightScheduleType(FlightScheduleType.MULTIPLE);
                    break;
                case (3):
                    flightSchedulePlanSelected.setFlightScheduleType(FlightScheduleType.RECURRENTBYDAY);
                    System.out.print("Recurrence by how many days> ");
                    Integer recurrence = Integer.valueOf(scanner.nextLine().trim());
                    flightSchedulePlanSelected.setRecurrence(recurrence);
                    break;
                case (4):
                    flightSchedulePlanSelected.setFlightScheduleType(FlightScheduleType.RECURRENTBYWEEK);
                    break;
            }
            if (response == 3 || response == 4) {
                System.out.print("Enter End Date (yyyy-mm-dd)> ");
                String endDateString = scanner.nextLine().trim();
                DateFormat format = new SimpleDateFormat("year-month-day");
                Date endDateTime = format.parse(endDateString);
                flightSchedulePlanSelected.setEndDate(endDateTime);
            }
        } else if (response == 7) {
            System.out.print("Enter New Layover Duration (hr:min)> ");
            String layoverDurationString = scanner.nextLine().trim();
            DateFormat durationFormat = new SimpleDateFormat("hr:min");
            Date layoverDurationTime = durationFormat.parse(layoverDurationString);
            flightSchedulePlanSelected.setLayoverDuration(layoverDurationTime);
        }

    }
}
