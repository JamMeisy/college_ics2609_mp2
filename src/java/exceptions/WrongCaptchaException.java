/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 *
 * @author Lejan Juico
 */
public class WrongCaptchaException extends RuntimeException {
    public WrongCaptchaException (String e) {
        super(e);
    }
}
