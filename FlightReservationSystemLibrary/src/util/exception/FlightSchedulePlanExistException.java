/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Administrator
 */
public class FlightSchedulePlanExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightSchedulePlanExistException</code>
     * without detail message.
     */
    public FlightSchedulePlanExistException() {
    }

    /**
     * Constructs an instance of <code>FlightSchedulePlanExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightSchedulePlanExistException(String msg) {
        super(msg);
    }
}
