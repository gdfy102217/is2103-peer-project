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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity

public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigurationId;
    @Column(nullable = false, length = 64, unique = true)
    private String aircraftConfigurationName;
    @Min(1)
    @Max(4)
    private Integer numOfCabinClasses;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftType aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfiguration")
    private List<Flight> flights;
    
    @OneToMany(mappedBy = "aircraftConfiguration", cascade = CascadeType.PERSIST)
    private List<CabinClass> cabinClasses;

    public AircraftConfiguration() {
        
        cabinClasses = new ArrayList<>();
        flights = new ArrayList<>();
    }

    public AircraftConfiguration(String aircraftConfigurationName, Integer numOfCabinClasses) {
        this();
        
        this.aircraftConfigurationName = aircraftConfigurationName;
        this.numOfCabinClasses = numOfCabinClasses;
    }

    public Long getAircraftConfigurationId() {
        return aircraftConfigurationId;
    }

    public void setAircraftConfigurationId(Long aircraftConfigurationId) {
        this.aircraftConfigurationId = aircraftConfigurationId;
    }

    public String getAircraftConfigurationName() {
        return aircraftConfigurationName;
    }

    public void setAircraftConfigurationName(String aircraftConfigurationName) {
        this.aircraftConfigurationName = aircraftConfigurationName;
    }

    public Integer getNumOfCabinClasses() {
        return numOfCabinClasses;
    }

    public void setNumOfCabinClasses(Integer numOfCabinClasses) {
        this.numOfCabinClasses = numOfCabinClasses;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }
    
    public Integer getMaxSeatCapacity() {
        Integer maximumSeatCapacity = 0;
        
        for (CabinClass cabinClass: cabinClasses) {
            maximumSeatCapacity += cabinClass.getCabinClassConfiguration().getCabinClassCapacity();
        }
        
        return maximumSeatCapacity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigurationId != null ? aircraftConfigurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftConfigurationId fields are not set
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.aircraftConfigurationId == null && other.aircraftConfigurationId != null) || (this.aircraftConfigurationId != null && !this.aircraftConfigurationId.equals(other.aircraftConfigurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Aircraft Configuration Name = " + aircraftConfigurationName + ", Num Of Cabin Classes = " + numOfCabinClasses 
                + ", Aircraft Type = " + aircraftType.getAircraftTypeName();
    }

    public List<CabinClass> getCabinClasses() {
        return cabinClasses;
    }

    public void setCabinClasses(List<CabinClass> cabinClasses) {
        this.cabinClasses = cabinClasses;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    
}
