/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.CabinClassSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClass;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.Partner;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassType;
import util.enumeration.EmployeeType;
import util.exception.AircraftConfigurationExistExcetpion;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeExistException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportExistException;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeExistException;
import util.exception.FlightExistException;
import util.exception.FlightRouteDisabledException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.PartnerExistException;

/**
 *
 * @author Administrator
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "FlightSessionBeanLocal")
    private FlightSessionBeanLocal flightSessionBeanLocal;

    @EJB(name = "FlightRouteSessionBeanLocal")
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;

    @EJB(name = "AircraftConfigurationSessionBeanLocal")
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @EJB(name = "AirportSessionBeanLocal")
    private AirportSessionBeanLocal airportSessionBeanLocal;
    
    @EJB(name = "AircraftTypeSessionBeanLocal")
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    
    public DataInitSessionBean()
    {
    }

    
    @PostConstruct
    public void postConstruct()
    {
        if(em.find(Employee.class, 1l) == null)
        {
            initialiseEmployee();
        }
        if(em.find(Partner.class, 1l) == null)
        {
            initialisePartner();
        }
        if(em.find(Airport.class, 1l) == null)
        {
            initialiseAirport();
        }
        if(em.find(AircraftType.class, 1l) == null)
        {
            initialiseAircraftType();
        }
        if(em.find(AircraftConfiguration.class, 1l) == null)
        {
            initialiseAircraftConfiguration();
        }
        if(em.find(FlightRoute.class, 1l) == null)
        {
            initialiseFlightRoute();
        }
        if(em.find(Flight.class, 1l) == null)
        {
            initialiseFlight();
        }
    }
    
    private void initialiseEmployee()
    {
        try {
            employeeSessionBeanLocal.createNewEmployee(new Employee("Sales Manager", "salesmanager", "password", EmployeeType.SALESMANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Fleet Manager", "fleetmanager", "password", EmployeeType.FLEETMANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Route Planner", "routeplanner", "password", EmployeeType.ROUTEPLANNER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Schedule Manager", "schedulemanager", "password", EmployeeType.SCHEDULEMANAGER));
        } catch (EmployeeExistException | GeneralException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }
    
    private void initialisePartner()
    {
        try {
            customerSessionBeanLocal.createNewPartner(new Partner("Holiday.com","holidaydotcom","password"));
        } catch (PartnerExistException | GeneralException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    private void initialiseAirport()
    {
        try {
            airportSessionBeanLocal.createNewAirport(new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore", "+8"));
            airportSessionBeanLocal.createNewAirport(new Airport("Hong Kong", "HKG", "Chek Lap Kok", "HongKong", "China", "+8"));
            airportSessionBeanLocal.createNewAirport(new Airport("Taoyuan", "TPE", "Taipei", "Taiwan Province", "China", "+8"));
            airportSessionBeanLocal.createNewAirport(new Airport("Narita", "NRT", "Narita", "Chiba", "Japan", "+9"));
            airportSessionBeanLocal.createNewAirport(new Airport("Sydney", "SYD", "Sydney", "New South Wales", "Australia", "+11"));
        } catch (AirportExistException | GeneralException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public void initialiseAircraftType()
    {
        try {
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing 737", 200));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing 747", 400));
        } catch (AircraftTypeExistException | GeneralException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public void initialiseAircraftConfiguration()
    {
        try {
            AircraftType boeing737 = aircraftTypeSessionBeanLocal.retrieveAircraftTypeByName("Boeing 737");
            AircraftType boeing747 = aircraftTypeSessionBeanLocal.retrieveAircraftTypeByName("Boeing 747");
            
            //Boeing 737 All Economy
            AircraftConfiguration boeing737AllEconomy = new AircraftConfiguration("Boeing737AllEconomy", 1);
            List<CabinClass> boeing737AllEconomyList = new ArrayList<>();
            
            CabinClassConfiguration allEconomyEconomyConfig = new CabinClassConfiguration(1, 30, 6, "3-3", 180);
            boeing737AllEconomyList.add(new CabinClass(CabinClassType.ECONOMYCLASS, allEconomyEconomyConfig));
            
            aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(boeing737AllEconomy, boeing737, boeing737AllEconomyList);
            
            //Boeing 737 Three Classes
            AircraftConfiguration boeing737ThreeClasses = new AircraftConfiguration("Boeing737ThreeClasses", 3);
            List<CabinClass> boeing737ThreeClassesList = new ArrayList<>();
            
            
            CabinClassConfiguration boeing737ThreeClassesFirst = new CabinClassConfiguration(1, 5, 2, "1-1", 10);
            boeing737ThreeClassesList.add(new CabinClass(CabinClassType.FIRSTCLASS, boeing737ThreeClassesFirst));
            CabinClassConfiguration boeing737ThreeClassesBusiness = new CabinClassConfiguration(1, 5, 4, "2-2", 20);
            boeing737ThreeClassesList.add(new CabinClass(CabinClassType.BUSINESSCLASS, boeing737ThreeClassesBusiness));
            CabinClassConfiguration boeing737ThreeClassesEconomy = new CabinClassConfiguration(1, 25, 6, "3-3", 150);
            boeing737ThreeClassesList.add(new CabinClass(CabinClassType.ECONOMYCLASS, boeing737ThreeClassesEconomy));
            
            aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(boeing737ThreeClasses, boeing737, boeing737ThreeClassesList);
            
            //Boeing 747 All Economy
            AircraftConfiguration boeing747AllEconomy = new AircraftConfiguration("Boeing747AllEconomy", 1);
            List<CabinClass> boeing747AllEconomyList = new ArrayList<>();
            
            CabinClassConfiguration boeing747AllEconomyEconomy = new CabinClassConfiguration(2, 38, 10, "3-4-3", 380);
            boeing747AllEconomyList.add(new CabinClass(CabinClassType.ECONOMYCLASS, boeing747AllEconomyEconomy));
            
            aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(boeing747AllEconomy, boeing747, boeing747AllEconomyList);
            
            //Boeing 747 Three Classes
            AircraftConfiguration boeing747ThreeClasses = new AircraftConfiguration("Boeing747ThreeClasses", 3);
            List<CabinClass> boeing747ThreeClassesList = new ArrayList<>();
            
            
            CabinClassConfiguration boeing747ThreeClassesFirst = new CabinClassConfiguration(1, 5, 2, "1-1", 10);
            boeing747ThreeClassesList.add(new CabinClass(CabinClassType.FIRSTCLASS, boeing747ThreeClassesFirst));
            CabinClassConfiguration boeing747ThreeClassesBusiness = new CabinClassConfiguration(2, 5, 6, "2-2-2", 30);
            boeing747ThreeClassesList.add(new CabinClass(CabinClassType.BUSINESSCLASS, boeing747ThreeClassesBusiness));
            CabinClassConfiguration boeing747ThreeClassesEconomy = new CabinClassConfiguration(2, 32, 10, "3-4-3", 320);
            boeing747ThreeClassesList.add(new CabinClass(CabinClassType.ECONOMYCLASS, boeing747ThreeClassesEconomy));
            
            aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(boeing747ThreeClasses, boeing747, boeing747ThreeClassesList);
        } catch (AircraftTypeNotFoundException | AircraftConfigurationExistExcetpion | GeneralException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public void initialiseFlightRoute()
    {
        try {
            Long newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("SIN", "HKG");
            Long newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("HKG", "SIN");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
            newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("SIN", "TPE");
            newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("TPE", "SIN");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
            newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("SIN", "NRT");
            newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("NRT", "SIN");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
            newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("HKG", "NRT");
            newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("NRT", "HKG");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
            newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("TPE", "NRT");
            newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("NRT", "TPE");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
            newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("SIN", "SYD");
            newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("SYD", "SIN");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
            newFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("SYD", "NRT");
            newComplementaryFlightRouteId = flightRouteSessionBeanLocal.createNewFlightRoute("NRT", "SYD");
            flightRouteSessionBeanLocal.associateComplementaryFlightRoute(newFlightRouteId, newComplementaryFlightRouteId);
            
        } catch (FlightRouteExistException | GeneralException | AirportNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public void initialiseFlight()
    {
//        try {
//            AircraftConfiguration boeing737AllEconomy = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing737AllEconomy");
//            AircraftConfiguration boeing737ThreeClasses = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing737ThreeClasses");
//            AircraftConfiguration boeing747ThreeClasses = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing747ThreeClasses");
//            
////            Flight ml111 = new Flight("ML111");
////            flightSessionBeanLocal.createNewFlight(ml111, "SIN", "HKG", "Boeing737ThreeClasses");
////            
////            Flight ml211 = new Flight("ML211");
////            flightSessionBeanLocal.createNewFlight(ml211, "SIN", "TPE", "Boeing737ThreeClasses");
//        } catch (AircraftConfigurationNotFoundException | FlightExistException | GeneralException 
//                | FlightRouteNotFoundException | FlightRouteDisabledException ex) {
//            System.out.println("Error: " + ex.getMessage());
//        }
    }
}
