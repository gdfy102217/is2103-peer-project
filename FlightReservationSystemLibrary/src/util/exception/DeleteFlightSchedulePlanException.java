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
public class DeleteFlightSchedulePlanException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFlightSchedulePlanException</code>
     * without detail message.
     */
    public DeleteFlightSchedulePlanException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlightSchedulePlanException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlightSchedulePlanException(String msg) {
        super(msg);
    }
}
