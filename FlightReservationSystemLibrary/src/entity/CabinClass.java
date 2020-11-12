/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.CabinClassType;

/**
 *
 * @author Administrator
 */

@Entity

public class CabinClass implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassId;
    @Column(nullable = false)
    private CabinClassType cabinClassType;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    @OneToOne()
    private CabinClassConfiguration cabinClassConfiguration;
    
    private Integer numOfReservedSeats;
    private Integer numOfAvailableSeats;
    private Integer numOfBalanceSeats;
    
    public CabinClass() {
        this.numOfReservedSeats = 0;
        
    }

    public CabinClass(FlightSchedule flightSchedule, CabinClassConfiguration cabinClassConfiguration) {
        this();
        this.flightSchedule = flightSchedule;
        this.cabinClassConfiguration = cabinClassConfiguration;
        this.numOfAvailableSeats = cabinClassConfiguration.getMaxSeatCapacity();
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

    public Integer getNumOfReservedSeats() {
        return numOfReservedSeats;
    }

    public void setNumOfReservedSeats(Integer numOfReservedSeats) {
        this.numOfReservedSeats = numOfReservedSeats;
    }

    public Long getCabinClassId() {
        return cabinClassId;
    }

    public void setCabinClassId(Long cabinClassId) {
        this.cabinClassId = cabinClassId;
    }

    public Integer getNumOfAvailableSeats() {
        return numOfAvailableSeats;
    }

    public void setNumOfAvailableSeats(Integer numOfAvailableSeats) {
        this.numOfAvailableSeats = numOfAvailableSeats;
    }

    public Integer getNumOfBalanceSeats() {
        return numOfAvailableSeats - numOfReservedSeats;
    }

    public void setNumOfBalanceSeats(Integer numOfBalanceSeats) {
        this.numOfBalanceSeats = numOfBalanceSeats;
    }

    public CabinClassType getCabinClassType() {
        return cabinClassType;
    }

    public void setCabinClassType(CabinClassType cabinClassType) {
        this.cabinClassType = cabinClassType;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }

    @Override
    public String toString() {
        return "Cabin Class Type = " + cabinClassType + ", Cabin Class Configuration = [" + cabinClassConfiguration + ']';
    }
    
    
}
