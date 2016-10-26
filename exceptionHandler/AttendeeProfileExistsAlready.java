package org.iqvis.nvolv3.exceptionHandler;

public class AttendeeProfileExistsAlready extends Exception{
	
	public AttendeeProfileExistsAlready(String email){
		super("Attendee Profile Exists Already Against Email : "+email);
	}

}
