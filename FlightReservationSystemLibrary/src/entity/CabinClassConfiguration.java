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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity

public class CabinClassConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassConfigurationId;
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
    @Min(0)
    @Column(nullable = false)
    private Integer cabinClassCapacity;
    
    
    @OneToMany(mappedBy = "cabinClassConfiguration", fetch = FetchType.EAGER)
    private List<Fare> fares;

    public CabinClassConfiguration() {
        this.fares = new ArrayList<>();
    }

    public CabinClassConfiguration(Integer numOfAisles, Integer numOfRows, Integer numOfSeatsAbreast, String seatingConfigurationPerColumn, Integer cabinClassCapacity) {
        this.numOfAisles = numOfAisles;
        this.numOfRows = numOfRows;
        this.numOfSeatsAbreast = numOfSeatsAbreast;
        this.seatingConfigurationPerColumn = seatingConfigurationPerColumn;
        this.cabinClassCapacity = cabinClassCapacity;
    }

    public Long getCabinClassConfigurationId() {
        return cabinClassConfigurationId;
    }

    public void setCabinClassConfigurationId(Long cabinClassConfigurationId) {
        this.cabinClassConfigurationId = cabinClassConfigurationId;
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

    public Integer getCabinClassCapacity() {
        return cabinClassCapacity;
    }

    public void setCabinClassCapacity(Integer cabinClassCapacity) {
        this.cabinClassCapacity = cabinClassCapacity;
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
        return "No. Of Aisles = " + numOfAisles + ", No. Of Rows = " + numOfRows +
                ", No. Of Seats Abreast = " + numOfSeatsAbreast + ", Seating Configuration Per Column = " + seatingConfigurationPerColumn;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    
    
}
