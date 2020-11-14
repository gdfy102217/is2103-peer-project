/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlTransient;
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
    
    @ManyToOne()
    @JoinColumn()
    private FlightSchedule flightSchedule;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private CabinClassConfiguration cabinClassConfiguration;
    
    @Min(0)
    private Integer numOfReservedSeats;
    @Min(0)
    private Integer numOfAvailableSeats;
    
    public CabinClass() {
        this.numOfReservedSeats = 0;
    }

    public CabinClass(CabinClassType cabinClassType, CabinClassConfiguration cabinClassConfiguration) {
        this();
        this.cabinClassType = cabinClassType;
        this.cabinClassConfiguration = cabinClassConfiguration;
        this.numOfAvailableSeats = cabinClassConfiguration.getCabinClassCapacity();
    }

    @XmlTransient
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
