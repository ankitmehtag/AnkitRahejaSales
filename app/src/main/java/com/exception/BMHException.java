/**
 * 
 */
package com.exception;


/**
 * It Overrides the Exception class only when there network error occurs .And
 * changes it to custom message as <b> Connection Error </b> .
 * 
 * @author 
 */
public class BMHException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 315545937906781320L;
	private String errorMessage;

	public BMHException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * <p>
	 * <b> public String getErrorMessage({@link Exception} e) </b>
	 * </p>
	 * Returns Custom error message as <b>Connection Error </b> when network
	 * error occurs. And also changeable accordingly .
	 * 
	 * @param e
	 *            - Throwing Exception .
	 * @return errorMessage
	 */
	public String getErrorMessage(Exception e) {
		errorMessage = "Connection Error";
		return errorMessage;
	}
}
