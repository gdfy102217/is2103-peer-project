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
public class ExceedMaximumSeatCapacityException extends Exception {

    /**
     * Creates a new instance of <code>ExceedMaximumSeatCapacityException</code>
     * without detail message.
     */
    public ExceedMaximumSeatCapacityException() {
    }

    /**
     * Constructs an instance of <code>ExceedMaximumSeatCapacityException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExceedMaximumSeatCapacityException(String msg) {
        super(msg);
    }
}
