/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.AircraftType;
import entity.Airport;
import entity.Employee;
import entity.Partner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeType;
import util.exception.AircraftTypeExistException;
import util.exception.AirportExistException;
import util.exception.EmployeeExistException;
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
    }
    
    private void initialiseEmployee()
    {
        try {
            employeeSessionBeanLocal.createNewEmployee(new Employee("Sales Manager", "sales", "password", EmployeeType.SALESMANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Fleet Manager", "fleet", "password", EmployeeType.FLEETMANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Route Planner", "route", "password", EmployeeType.ROUTEPLANNER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Schedule Manager", "schedule", "password", EmployeeType.SCHEDULEMANAGER));
        } catch (EmployeeExistException | GeneralException ex) {
            System.out.println(ex);
        }

    }
    
    private void initialisePartner()
    {
        try {
            customerSessionBeanLocal.createNewPartner(new Partner("Partner","Test","partner","password"));
        } catch (PartnerExistException | GeneralException ex) {
            System.out.println(ex);
        }
    }
    
    private void initialiseAirport()
    {

        try {
            airportSessionBeanLocal.createNewAirport(new Airport("Changi International Airport", "SIN", "Singapore", "Singapore", "Singapore", "SGT"));
            airportSessionBeanLocal.createNewAirport(new Airport("HongKong International Airport", "HKG", "HongKong", "HongKongS.A.R.", "China", "HKT"));
            airportSessionBeanLocal.createNewAirport(new Airport("Taoyuan International Airport", "TPE", "Taipei", "Taiwan Province", "China", "CST"));
            airportSessionBeanLocal.createNewAirport(new Airport("Narita International Airport", "NRT", "Tokyo", "Tokyo-to", "Japan", "JST"));
            airportSessionBeanLocal.createNewAirport(new Airport("Beijing Capital International Airport", "PEK", "Beijing", "Beijing", "China", "CST"));
            airportSessionBeanLocal.createNewAirport(new Airport("Incheon International Airport", "ICN", "Incheon", "Incheon", "South Korea", "KST"));
            airportSessionBeanLocal.createNewAirport(new Airport("Los Angeles International Airport", "LAX", "Los Angeles", "California", "United States", "PST"));
            airportSessionBeanLocal.createNewAirport(new Airport("Heathrow International Airport", "LHR", "London", "London", "United Kingdom", "GMT"));
        } catch (AirportExistException | GeneralException ex) {
            System.out.println(ex);
        }

    }
    
    public void initialiseAircraftType()
    {
        try {
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A320", 180));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A350", 440));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A380", 853));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A330", 440));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("B737", 180));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("B787", 390));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("B777", 450));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("B747", 580));
        } catch (AircraftTypeExistException | GeneralException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
