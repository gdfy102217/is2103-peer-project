/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
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
import util.exception.AirportExistException;
import util.exception.EmployeeExistException;
import util.exception.GeneralException;

/**
 *
 * @author Administrator
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    private AirportSessionBeanLocal airportSessionBeanLocal;
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
    
    public void initialiseEmployee()
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
    
    public void initialisePartner()
    {
        
    }
    
    public void initialiseAirport()
    {
//        try {
//            airportSessionBeanLocal.createNewAirport(new Airport("Changi International Airport", "SIN", "Singapore", "Singapore", "Singapore"));
//            airportSessionBeanLocal.createNewAirport(new Airport("HongKong International Airport", "HKG", "HongKong", "HongKongS.A.R.", "China"));
//            airportSessionBeanLocal.createNewAirport(new Airport("Taoyuan International Airport", "TPE", "Taipei", "Taiwan Province", "China"));
//            airportSessionBeanLocal.createNewAirport(new Airport("Narita International Airport", "NRT", "Tokyo", "Tokyo-to", "Japan"));
//        } catch (AirportExistException | GeneralException ex) {
//            System.out.println(ex);
//        }
    }
    
    public void initialiseAircraftType()
    {
//        aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A320", 180));
//        aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A350", 440));
//        aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A380", 853));
//        aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("A330", 440));
    }

}
