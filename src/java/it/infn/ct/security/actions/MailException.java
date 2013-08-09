/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.actions;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class MailException extends Exception {

    /**
     * Creates a new instance of
     * <code>MailException</code> without detail message.
     */
    public MailException() {
    }

    /**
     * Constructs an instance of
     * <code>MailException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MailException(String msg) {
        super(msg);
    }
}
