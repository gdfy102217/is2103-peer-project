/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.CabinClass;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClassType;
import util.enumeration.FlightScheduleType;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.DeleteFlightScheduleException;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FareExistException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteDisabledException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.UpdateFlightSchedulePlanException;

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

    private Employee employee;

    public FlightOperationModule() {
    }

    public FlightOperationModule(FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, Employee employee) {
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
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
                    viewFlightSchedulePlanDetails();
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

    private void doCreateNewFlight() {
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
        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        
        if (flights.isEmpty()) {
            System.out.println("No Available Flights!\n");
        }
        
        for (Flight flight : flights) {
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
            for (CabinClass cabinClass : flight.getAircraftConfiguration().getCabinClasses()) {
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

            String departureDateTimeString;
            DateFormat format = new SimpleDateFormat("year-month-day hr:min");
            Date departureDateTime;
            String durationString;
            DateFormat durationFormat = new SimpleDateFormat("hr:min");
            Date durationTime;
            List<Fare> fares = new ArrayList<>();
            boolean option = true;
            Long firstDepartureTimeLong = Long.MIN_VALUE;
            FlightSchedulePlan newFlightSchedulePlan = new FlightSchedulePlan();
            Date endDateTime;
            Integer recurrence = 0;

            switch (response) {
                case (1):

                    System.out.println("*** Create Single Flight Schedule ***\n");
                    System.out.print("Enter Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                    departureDateTimeString = scanner.nextLine().trim();
                    departureDateTime = format.parse(departureDateTimeString);

                    System.out.print("Enter Flight Duration (hr:min)> ");
                    durationString = scanner.nextLine().trim();
                    durationTime = durationFormat.parse(durationString);

                    System.out.println("*** Create Single Flight Schedule :: Enter Fares ***\n");
                    option = true;
                    while (option) {
                        System.out.print("Enter Cabin Class Code> ");
                        String cabinClassCode = scanner.nextLine().trim();
                        System.out.print("Enter Fare Basis Code> ");
                        String fareBasisCode = scanner.nextLine().trim();
                        System.out.print("Enter Amount> $ ");
                        Double fareAmount = Double.valueOf(scanner.nextLine().trim());

                        CabinClassType cabinClassType;
                        if (cabinClassCode.charAt(0) == 'F') {
                            cabinClassType = CabinClassType.FIRSTCLASS;
                        } else if (cabinClassCode.charAt(0) == 'J') {
                            cabinClassType = CabinClassType.BUSINESSCLASS;
                        } else if (cabinClassCode.charAt(0) == 'W') {
                            cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                        } else {
                            cabinClassType = CabinClassType.ECONOMYCLASS;
                        }

                        Fare fare = new Fare(fareBasisCode, cabinClassType, fareAmount);
                        fares.add(fare);

                        System.out.print("Continue to create more fare? (Y/N)> ");
                        String optionString = scanner.nextLine().trim();
                        if (optionString.charAt(0) == 'N') {
                            option = false;
                        }
                    }
                    newFlightSchedulePlan = new FlightSchedulePlan(FlightScheduleType.SINGLE, fares, departureDateTime.getTime()); //rmb to merge flight
                    newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.createNewSingleFlightSchedulePlan(newFlightSchedulePlan, flight, departureDateTime, durationTime);

                    System.out.println("Flight Schedule Plan with ID: " + newFlightSchedulePlan.getFlightSchedulePlanId() + " is created successfully!\n");
                    break;
                case (2):
                    System.out.println("*** Create Multiple Flight Schedule ***\n");
                    System.out.println("*** Create Single Flight Schedule :: Enter Schedules ***\n");
                    List<Date> departureDateTimes = new ArrayList<>();
                    List<Date> durationTimes = new ArrayList<>();

                    option = true;
                    while (option) {
                        System.out.print("Enter Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                        departureDateTimeString = scanner.nextLine().trim();
                        departureDateTime = format.parse(departureDateTimeString);
                        if (departureDateTimes.isEmpty()) {
                            firstDepartureTimeLong = departureDateTime.getTime();
                        }
                        departureDateTimes.add(departureDateTime);

                        System.out.print("Enter Flight Duration (hr:min)> ");
                        durationString = scanner.nextLine().trim();
                        durationTime = durationFormat.parse(durationString);
                        durationTimes.add(durationTime);

                        System.out.print("Continue to create more schedules? (Y/N)> ");
                        String optionString = scanner.nextLine().trim();
                        if (optionString.charAt(0) == 'N') {
                            option = false;
                        }
                    }

                    System.out.println("*** Create Multiple Flight Schedule :: Enter Fares ***\n");
                    option = true;
                    while (option) {
                        System.out.print("Enter Cabin Class Code> ");
                        String cabinClassCode = scanner.nextLine().trim();
                        System.out.print("Enter Fare Basis Code> ");
                        String fareBasisCode = scanner.nextLine().trim();
                        System.out.print("Enter Amount> $ ");
                        Double fareAmount = Double.valueOf(scanner.nextLine().trim());

                        CabinClassType cabinClassType;
                        if (cabinClassCode.charAt(0) == 'F') {
                            cabinClassType = CabinClassType.FIRSTCLASS;
                        } else if (cabinClassCode.charAt(0) == 'J') {
                            cabinClassType = CabinClassType.BUSINESSCLASS;
                        } else if (cabinClassCode.charAt(0) == 'W') {
                            cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                        } else {
                            cabinClassType = CabinClassType.ECONOMYCLASS;
                        }

                        Fare fare = new Fare(fareBasisCode, cabinClassType, fareAmount);
                        fares.add(fare);

                        System.out.print("Continue to create more fare? (Y/N)> ");
                        String optionString = scanner.nextLine().trim();
                        if (optionString.charAt(0) == 'N') {
                            option = false;
                        }
                    }
                    newFlightSchedulePlan = new FlightSchedulePlan(FlightScheduleType.MULTIPLE, fares, firstDepartureTimeLong); //rmb to merge flight
                    newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.createNewMultipleFlightSchedulePlan(newFlightSchedulePlan, flight, departureDateTimes, durationTimes);

                    System.out.println("Flight Schedule Plan with ID: " + newFlightSchedulePlan.getFlightSchedulePlanId() + " is created successfully!\n");
                    break;
                case (3):
                    System.out.println("*** Create Recurrent by Day Schedule ***\n");
                    System.out.print("Recurrence by how many days> ");
                    recurrence = Integer.valueOf(scanner.nextLine().trim());
                    System.out.print("Enter Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                    departureDateTimeString = scanner.nextLine().trim();
                    departureDateTime = format.parse(departureDateTimeString);
                    System.out.print("Enter Flight Duration (hr:min)> ");
                    durationString = scanner.nextLine().trim();
                    durationTime = durationFormat.parse(durationString);

                    System.out.print("Enter End Date (yyyy-mm-dd)> ");
                    String endDateString = scanner.nextLine().trim();
                    format = new SimpleDateFormat("year-month-day");
                    endDateTime = format.parse(endDateString);

                    System.out.println("*** Create Recurrent by Day Flight Schedule :: Enter Fares ***\n");
                    option = true;
                    while (option) {
                        System.out.print("Enter Cabin Class Code> ");
                        String cabinClassCode = scanner.nextLine().trim();
                        System.out.print("Enter Fare Basis Code> ");
                        String fareBasisCode = scanner.nextLine().trim();
                        System.out.print("Enter Amount> $ ");
                        Double fareAmount = Double.valueOf(scanner.nextLine().trim());

                        CabinClassType cabinClassType;
                        if (cabinClassCode.charAt(0) == 'F') {
                            cabinClassType = CabinClassType.FIRSTCLASS;
                        } else if (cabinClassCode.charAt(0) == 'J') {
                            cabinClassType = CabinClassType.BUSINESSCLASS;
                        } else if (cabinClassCode.charAt(0) == 'W') {
                            cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                        } else {
                            cabinClassType = CabinClassType.ECONOMYCLASS;
                        }

                        Fare fare = new Fare(fareBasisCode, cabinClassType, fareAmount);
                        fares.add(fare);

                        System.out.print("Continue to create more fare? (Y/N)> ");
                        String optionString = scanner.nextLine().trim();
                        if (optionString.charAt(0) == 'N') {
                            option = false;
                        }
                    }

                    newFlightSchedulePlan = new FlightSchedulePlan(FlightScheduleType.RECURRENTBYDAY, fares, departureDateTime.getTime()); //rmb to merge flight
                    newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.createNewRecurrentFlightSchedulePlan(newFlightSchedulePlan, flight, departureDateTime, durationTime, recurrence, endDateTime);

                    System.out.println("Flight Schedule Plan with ID: " + newFlightSchedulePlan.getFlightSchedulePlanId() + " is created successfully!\n");
                    break;
                case (4):
                    System.out.println("*** Create Recurrent by Week Schedule ***\n");
                    System.out.print("Enter Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                    format = new SimpleDateFormat("year-month-day hr:min");
                    departureDateTimeString = scanner.nextLine().trim();
                    departureDateTime = format.parse(departureDateTimeString);
                    System.out.print("Enter Flight Duration (hr:min)> ");
                    durationString = scanner.nextLine().trim();
                    durationTime = durationFormat.parse(durationString);

                    System.out.print("Enter End Date (yyyy-mm-dd)> ");
                    endDateString = scanner.nextLine().trim();
                    format = new SimpleDateFormat("year-month-day");
                    endDateTime = format.parse(endDateString);

                    System.out.println("*** Create Recurrent by Week Flight Schedule :: Enter Fares ***\n");
                    option = true;
                    while (option) {
                        System.out.print("Enter Cabin Class Code> ");
                        String cabinClassCode = scanner.nextLine().trim();
                        System.out.print("Enter Fare Basis Code> ");
                        String fareBasisCode = scanner.nextLine().trim();
                        System.out.print("Enter Amount> $ ");
                        Double fareAmount = Double.valueOf(scanner.nextLine().trim());

                        CabinClassType cabinClassType;
                        if (cabinClassCode.charAt(0) == 'F') {
                            cabinClassType = CabinClassType.FIRSTCLASS;
                        } else if (cabinClassCode.charAt(0) == 'J') {
                            cabinClassType = CabinClassType.BUSINESSCLASS;
                        } else if (cabinClassCode.charAt(0) == 'W') {
                            cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                        } else {
                            cabinClassType = CabinClassType.ECONOMYCLASS;
                        }

                        Fare fare = new Fare(fareBasisCode, cabinClassType, fareAmount);
                        fares.add(fare);

                        System.out.print("Continue to create more fare? (Y/N)> ");
                        String optionString = scanner.nextLine().trim();
                        if (optionString.charAt(0) == 'N') {
                            option = false;
                        }
                    }

                    recurrence = 7;
                    newFlightSchedulePlan = new FlightSchedulePlan(FlightScheduleType.RECURRENTBYDAY, fares, departureDateTime.getTime()); //rmb to merge flight
                    newFlightSchedulePlan = flightSchedulePlanSessionBeanRemote.createNewRecurrentFlightSchedulePlan(newFlightSchedulePlan, flight, departureDateTime, durationTime, recurrence, endDateTime);

                    System.out.println("Flight Schedule Plan with ID: " + newFlightSchedulePlan.getFlightSchedulePlanId() + " is created successfully!\n");
                    break;
                default:
                    break;
            }

            //complementary flight schedule plan
            if (flight.getComplementaryReturnFlight() != null) {
                System.out.print("Create a complementary return flight schedule plan? (Y/N)> ");

                if (scanner.nextLine().trim().equals("Y")) {
                    System.out.print("Enter Layover Duration (hr:min)> ");
                    String layoverDurationString = scanner.nextLine().trim();
                    durationFormat = new SimpleDateFormat("hr:min");
                    Date layoverDurationTime = durationFormat.parse(layoverDurationString);

                    Long returnFLightSchedulePlanId = flightSchedulePlanSessionBeanRemote.createReturnFlightSchedulePlan(newFlightSchedulePlan, flight, layoverDurationTime);
                    System.out.println("Return Flight Schedule Plan with ID: " + returnFLightSchedulePlanId + " is created successfully!\n");
                }
            }

        } catch (FlightNotFoundException | ParseException | FlightSchedulePlanExistException | GeneralException | FlightScheduleExistException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void doViewAllFlightSchedulePlans() {
        System.out.println("*** FRSManagement :: Flight Operation Module :: View All Flight Schedule Plans ***\n");

        List<FlightSchedulePlan> flightSchedulePlans = flightSchedulePlanSessionBeanRemote.retrieveAllFlightSchedulePlans();
        if (flightSchedulePlans.isEmpty()) {
            System.out.println("No Available Flight Schedule Plans!\n");
        } else {

            for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
                System.out.println(flightSchedulePlan);
                if (flightSchedulePlan.getComplementaryReturnSchedulePlan() != null) {
                    System.out.println("Complementary return schedule plan [ " + flightSchedulePlan.getComplementaryReturnSchedulePlan() + " ]");
                }
            }
            System.out.println();
        }
    }

    private void viewFlightSchedulePlanDetails() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("*** FRSManagement :: Flight Operation Module :: View Flight Schedule Plan Details ***\n");
            System.out.print("Enter Flight Number of the Flight Schedule Plan> ");
            String flightNumber = scanner.nextLine().trim();

            List<FlightSchedulePlan> flightSchedulePlans = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlansByFlightNumber(flightNumber);
            Integer selection = 1;
            for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
                System.out.println("No." + selection + " " + flightSchedulePlan);
                selection++;
            }

            System.out.print("Enter no. to view details> ");
            Integer userSelection = Integer.valueOf(scanner.nextLine().trim());
            FlightSchedulePlan flightSchedulePlanSelected = flightSchedulePlans.get(userSelection - 1);

            System.out.println("\nFlight O-D pair=[" + flightSchedulePlanSelected.getFlight() + "]");
            System.out.println("Flight schedule: ");
            for (FlightSchedule flightSchedule : flightSchedulePlanSelected.getFlightSchedules()) {
                System.out.println("\t" + flightSchedule);
            }
            System.out.println("Fare: ");
            for (Fare fare : flightSchedulePlanSelected.getFares()) {
                System.out.println("\t" + fare);
            }

            System.out.print("Update details of this flight schedule plan? (Y/N)> ");
            if (scanner.nextLine().trim().equals("Y")) {
                updateFlightSchedulePlan(flightSchedulePlanSelected);
            }

            System.out.print("Delete this flight schedule plan? (Y/N)> ");
            if (scanner.nextLine().trim().equals("Y")) {
                flightSchedulePlanSessionBeanRemote.deleteFlightSchedulePlan(flightSchedulePlanSelected);
                System.out.println("Flight Schedule Plan deleted Successfully!");
            }
        } catch (FlightSchedulePlanNotFoundException | DeleteFlightScheduleException | ParseException | FlightScheduleExistException
                | GeneralException | FareExistException | FlightNotFoundException | DeleteFlightSchedulePlanException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlanSelected) throws DeleteFlightScheduleException, ParseException,
            FlightScheduleExistException, GeneralException, FareExistException, FlightNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Operation Module :: Update Flight Schedule Plan ***\n");
        System.out.print("Enter New Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);

        List<Fare> fares = new ArrayList<>();

        System.out.println("Select Type of Flight Schedule Plan> ");
        System.out.println("1: Update Fares");
        System.out.println("2: Update Flight Schedules");
        System.out.print("> ");
        Integer response = Integer.valueOf(scanner.nextLine().trim());

        if (response == 1) {

            System.out.println("*** FRSManagement :: Flight Operation Module :: Update Flight Schedule Plan :: Update Fares***\n");
            boolean option = true;
            while (option) {
                System.out.print("Enter Cabin Class Code> ");
                String cabinClassCode = scanner.nextLine().trim();
                System.out.print("Enter Fare Basis Code> ");
                String fareBasisCode = scanner.nextLine().trim();
                System.out.print("Enter Amount> $ ");
                Double fareAmount = Double.valueOf(scanner.nextLine().trim());

                CabinClassType cabinClassType;
                if (cabinClassCode.charAt(0) == 'F') {
                    cabinClassType = CabinClassType.FIRSTCLASS;
                } else if (cabinClassCode.charAt(0) == 'J') {
                    cabinClassType = CabinClassType.BUSINESSCLASS;
                } else if (cabinClassCode.charAt(0) == 'W') {
                    cabinClassType = CabinClassType.PREMIUMECONOMYCLASS;
                } else {
                    cabinClassType = CabinClassType.ECONOMYCLASS;
                }

                Fare fare = new Fare(fareBasisCode, cabinClassType, fareAmount);
                fares.add(fare);

                System.out.print("Continue to create more fare? (Y/N)> ");
                String optionString = scanner.nextLine().trim();
                if (optionString.charAt(0) == 'N') {
                    option = false;
                }
            }
        } else if (response == 2) {
            System.out.println("*** FRSManagement :: Flight Operation Module :: Update Flight Schedule Plan :: Update Flight Schedules***\n");

            String departureDateTimeString;
            DateFormat format = new SimpleDateFormat("year-month-day hr:min");
            Date departureDateTime;
            String durationString;
            DateFormat durationFormat = new SimpleDateFormat("hr:min");
            Date durationTime;
            boolean option = true;
            Long firstDepartureTimeLong = Long.MIN_VALUE;
            FlightSchedulePlan newFlightSchedulePlan = new FlightSchedulePlan();
            Date endDateTime;
            Integer recurrence = 0;

            if (flightSchedulePlanSelected.getFlightScheduleType().equals(FlightScheduleType.SINGLE)) {
                try {
                    System.out.println("*** Update Single Flight Schedule ***\n");
                    System.out.print("Enter New Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                    departureDateTimeString = scanner.nextLine().trim();
                    departureDateTime = format.parse(departureDateTimeString);

                    System.out.print("Enter Flight Duration (hr:min)> ");
                    durationString = scanner.nextLine().trim();
                    durationTime = durationFormat.parse(durationString);

                    flightSchedulePlanSessionBeanRemote.updateSingleFlightSchedule(flightSchedulePlanSelected,
                            flightSchedulePlanSelected.getFlightSchedules().get(0), departureDateTime, durationTime);
                    System.out.println("Flight Schedule Plan with ID: " + flightSchedulePlanSelected.getFlightSchedulePlanId() + " is created successfully!\n");
                } catch (UpdateFlightSchedulePlanException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

            } else if (flightSchedulePlanSelected.getFlightScheduleType().equals(FlightScheduleType.MULTIPLE)) {

                try {
                    System.out.println("*** Update Multiple Flight Schedule ***\n");
                    System.out.println("*** Current Flight schedules Under The Plan ***\n");

                    Integer selection = 1;
                    for (FlightSchedule flightSchedule : flightSchedulePlanSelected.getFlightSchedules()) {
                        System.out.println("No." + selection + " " + flightSchedule);
                        selection++;
                    }

                    System.out.print("Enter no. to update the flight schedule> ");
                    Integer userSelection = Integer.valueOf(scanner.nextLine().trim());
                    FlightSchedule flightScheduleSelected = flightSchedulePlanSelected.getFlightSchedules().get(userSelection - 1);

                    System.out.print("Enter New Departure Date/Time (yyyy-mm-dd hh:mm)> ");
                    departureDateTimeString = scanner.nextLine().trim();
                    departureDateTime = format.parse(departureDateTimeString);

                    System.out.print("Enter Flight Duration (hr:min)> ");
                    durationString = scanner.nextLine().trim();
                    durationTime = durationFormat.parse(durationString);

                    flightSchedulePlanSessionBeanRemote.updateSingleFlightSchedule(flightSchedulePlanSelected, flightScheduleSelected, departureDateTime, durationTime);
                    System.out.println("Flight Schedule Plan with ID: " + flightSchedulePlanSelected.getFlightSchedulePlanId() + " is created successfully!\n");
                } catch (UpdateFlightSchedulePlanException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

            } else if (flightSchedulePlanSelected.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYDAY)) {

                try {
                    System.out.print("Recurrence by how many days> ");
                    recurrence = Integer.valueOf(scanner.nextLine().trim());

                    System.out.print("Enter End Date (yyyy-mm-dd)> ");
                    String endDateString = scanner.nextLine().trim();
                    format = new SimpleDateFormat("year-month-day");
                    endDateTime = format.parse(endDateString);

                    flightSchedulePlanSessionBeanRemote.updateRecurrentDayFlightSchedule(flightSchedulePlanSelected, recurrence, endDateTime);
                    System.out.println("Flight Schedule Plan with ID: " + flightSchedulePlanSelected.getFlightSchedulePlanId() + " is created successfully!\n");
                } catch (UpdateFlightSchedulePlanException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

            } else if (flightSchedulePlanSelected.getFlightScheduleType().equals(FlightScheduleType.RECURRENTBYWEEK)) {
                try {
                    System.out.print("Enter End Date (yyyy-mm-dd)> ");
                    String endDateString = scanner.nextLine().trim();
                    format = new SimpleDateFormat("year-month-day");
                    endDateTime = format.parse(endDateString);

                    flightSchedulePlanSessionBeanRemote.updateRecurrentWeekFlightSchedule(flightSchedulePlanSelected, endDateTime);
                    System.out.println("Flight Schedule Plan with ID: " + flightSchedulePlanSelected.getFlightSchedulePlanId() + " is created successfully!\n");
                } catch (UpdateFlightSchedulePlanException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }
}
