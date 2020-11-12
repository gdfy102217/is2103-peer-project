/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Administrator
 */
public class CabinClass {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassId;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;
    
    @OneToOne()
    private CabinClassConfiguration cabinClassConfiguration;
    
    private Integer numOfSeatsReserved;
    
    public CabinClass() {
        this.numOfSeatsReserved = 0;
    }

    public CabinClass(FlightSchedule flightSchedule, CabinClassConfiguration cabinClassConfiguration) {
        this();
        this.flightSchedule = flightSchedule;
        this.cabinClassConfiguration = cabinClassConfiguration;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public CabinClassConfiguration getCabinClassConfiguration() {
        return cabinClassConfiguration;
    }

    public void setCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) {
        this.cabinClassConfiguration = cabinClassConfiguration;
    }

    public Integer getNumOfSeatsReserved() {
        return numOfSeatsReserved;
    }

    public void setNumOfSeatsReserved(Integer numOfSeatsReserved) {
        this.numOfSeatsReserved = numOfSeatsReserved;
    }
    
    
}
