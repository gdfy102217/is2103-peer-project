/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import util.enumeration.CabinClassType;


@Entity

public class CabinClassConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassConfigurationId;
    @Column(nullable = false)
    private CabinClassType cabinClassType;
    @Min(0)
    @Max(2)
    @Column(nullable = false)
    private Integer numOfAisles;
    @Min(0)
    @Column(nullable = false)
    private Integer numOfRows;
    @Min(0)
    @Column(nullable = false)
    private Integer numOfSeatsAbreast;
    @Column(nullable = false)
    private String seatingConfigurationPerColumn;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;

    public CabinClassConfiguration() {
    }

    public CabinClassConfiguration(CabinClassType cabinClassType, Integer numOfAisles, Integer numOfRows, Integer numOfSeatsAbreast, String seatingConfigurationPerColumn, AircraftConfiguration aircraftConfiguration) {
        this.cabinClassType = cabinClassType;
        this.numOfAisles = numOfAisles;
        this.numOfRows = numOfRows;
        this.numOfSeatsAbreast = numOfSeatsAbreast;
        this.seatingConfigurationPerColumn = seatingConfigurationPerColumn;
        this.aircraftConfiguration = aircraftConfiguration;
    }
    
    

    public Long getCabinClassConfigurationId() {
        return cabinClassConfigurationId;
    }

    public void setCabinClassConfigurationId(Long cabinClassConfigurationId) {
        this.cabinClassConfigurationId = cabinClassConfigurationId;
    }

    public CabinClassType getCabinClassType() {
        return cabinClassType;
    }

    public void setCabinClassType(CabinClassType cabinClassType) {
        this.cabinClassType = cabinClassType;
    }

    public Integer getNumOfAisles() {
        return numOfAisles;
    }

    public void setNumOfAisles(Integer numOfAisles) {
        this.numOfAisles = numOfAisles;
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Integer numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Integer getNumOfSeatsAbreast() {
        return numOfSeatsAbreast;
    }

    public void setNumOfSeatsAbreast(Integer numOfSeatsAbreast) {
        this.numOfSeatsAbreast = numOfSeatsAbreast;
    }

    public String getSeatingConfigurationPerColumn() {
        return seatingConfigurationPerColumn;
    }

    public void setSeatingConfigurationPerColumn(String seatingConfigurationPerColumn) {
        this.seatingConfigurationPerColumn = seatingConfigurationPerColumn;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }
    
    public Integer getMaxSeatCapacity() {
        return this.numOfRows * this.numOfSeatsAbreast;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassConfigurationId != null ? cabinClassConfigurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassConfigurationId fields are not set
        if (!(object instanceof CabinClassConfiguration)) {
            return false;
        }
        CabinClassConfiguration other = (CabinClassConfiguration) object;
        if ((this.cabinClassConfigurationId == null && other.cabinClassConfigurationId != null) || (this.cabinClassConfigurationId != null && !this.cabinClassConfigurationId.equals(other.cabinClassConfigurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClassConfiguration[ id=" + cabinClassConfigurationId + " ]";
    }
    
}
