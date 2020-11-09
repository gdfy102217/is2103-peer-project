/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity

public class AircraftType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftTypeId;
    @Column(nullable = false, length = 18, unique = true)
    private String aircraftTypeName;
    private Integer maxPassengerSeatCapacity;
    
    @OneToMany(mappedBy = "aircraftType")
    List<AircraftConfiguration> configurations;

    public AircraftType() {
        configurations = new ArrayList<>();
    }

    public AircraftType(String aircraftTypeName, Integer maxPassengerSeatCapacity) {
        this();
        
        this.aircraftTypeName = aircraftTypeName;
        this.maxPassengerSeatCapacity = maxPassengerSeatCapacity;
    }
    
    

    public String getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(String aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
    }

    public Integer getMaxPassengerSeatCapacity() {
        return maxPassengerSeatCapacity;
    }

    public void setMaxPassengerSeatCapacity(Integer maxPassengerSeatCapacity) {
        this.maxPassengerSeatCapacity = maxPassengerSeatCapacity;
    }

    public List<AircraftConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<AircraftConfiguration> configurations) {
        this.configurations = configurations;
    }
    
    

    public Long getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(Long aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftTypeId != null ? aircraftTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftTypeId fields are not set
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.aircraftTypeId == null && other.aircraftTypeId != null) || (this.aircraftTypeId != null && !this.aircraftTypeId.equals(other.aircraftTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftType[ id=" + aircraftTypeId + " ]";
    }
    
}
