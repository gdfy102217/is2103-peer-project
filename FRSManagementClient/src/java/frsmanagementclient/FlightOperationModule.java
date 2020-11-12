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
import util.enumeration.FlightScheduleType;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.DeleteFlightScheduleException;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FareExistException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
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

    public FlightOperationModule(FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote, Employee employee) {
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
            System.out.println("6: Back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    try {
                        doCreateNewFlight();
                    } catch (FlightRouteNotFoundException | AircraftConfigurationNotFoundException | FlightExistException | GeneralException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 2) {
                    viewAllFlights();
                } else if (response == 3) {
                    try {
                        viewFlightDetails();
                    } catch (FlightNotFoundException | DeleteFlightException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 4) {

                    try {
                        doCreateNewFlightSchedulePlan();
                    } catch (FlightNotFoundException | ParseException | FlightScheduleExistException | GeneralException | FlightSchedulePlanExistException | FareExistException ex) {
                        System.out.println(ex);
                    }
                } else if (response == 5) {
                    doViewAllFlightSchedulePlans();
                } else if (response == 6) {
                    try {
                        viewFlightSchedulePlanDetails();
                    } catch (DeleteFlightScheduleException | ParseException | FlightScheduleExistException | GeneralException |
                            FareExistException | FlightSchedulePlanNotFoundException | DeleteFlightSchedulePlanException ex) {
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
        for (Flight flight : flightSessionBeanRemote.retrieveAllFlights()) {
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

    private void doCreateNewFlightSchedulePlan() throws FlightNotFoundException, ParseException, FlightScheduleExistException, GeneralException, FlightSchedulePlanExistException, FareExistException {
        Scanner scanner = new Scanner(System.in);
        FlightSchedulePlan newFlightSchedulePlan = new FlightSchedulePlan();
        System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Flight Schedule Plan ***\n");
        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        newFlightSchedulePlan.setFlight(flight);

        System.out.println("Select Type of Flight Schedule Plan> ");
        System.out.println("1: Single");
        System.out.println("2: Multiple");
        System.out.println("3: Recurrent schedule every n day");
        System.out.println("4: Recurrent schedule every week");
        System.out.print("> ");
        Integer response = Integer.valueOf(scanner.nextLine().trim());

        switch (response) {
            case (1):
                newFlightSchedulePlan.setFlightScheduleType(FlightScheduleType.SINGLE);
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
            System.out.println("*** Create Flight Schedule ***\n");
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

            newFlightSchedule.setFlightSchedulePlan(newFlightSchedulePlan);
            flightScheduleSessionBeanRemote.createNewFlightSchedule(newFlightSchedule);

            System.out.print("Continue to create more flight schedule? (Y/N)> ");
            if (scanner.nextLine().trim().equals("N")) {
                break;
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
    }

    private void doCreateFare(FlightSchedulePlan newFlightSchedulePlan) throws FareExistException, GeneralException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRSManagement :: Flight Operation Module :: Create New Fares ***\n");
        List<CabinClassConfiguration> cabinClassConfigurations = newFlightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassConfigurations();
        for (CabinClassConfiguration cabinClass : cabinClassConfigurations) {
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
                System.out.println("Complementary return schedule plan: " + flightSchedulePlan.getComplementaryReturnSchedulePlan());
            }
        }
    }

    private void viewFlightSchedulePlanDetails() throws DeleteFlightScheduleException, ParseException, FlightScheduleExistException, GeneralException, FareExistException, FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException {
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

    private void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlanSelected) throws DeleteFlightScheduleException, ParseException, FlightScheduleExistException, GeneralException, FareExistException {
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
