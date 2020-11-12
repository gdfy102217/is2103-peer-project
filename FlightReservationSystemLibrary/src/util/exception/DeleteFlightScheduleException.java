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
public class DeleteFlightScheduleException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFlightScheduleException</code>
     * without detail message.
     */
    public DeleteFlightScheduleException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlightScheduleException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlightScheduleException(String msg) {
        super(msg);
    }
}
