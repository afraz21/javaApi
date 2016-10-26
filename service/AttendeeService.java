package org.iqvis.nvolv3.service;

import java.util.List;
import java.util.Map;

import org.iqvis.nvolv.sinch.SinchUser;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.exceptionHandler.AttendeeProfileExistsAlready;
import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendeeService {

	public Attendee addAttendee(Attendee attendee) throws AttendeeProfileExistsAlready, ConstantNotExistsException;

	Attendee add(Attendee attendee) throws AttendeeProfileExistsAlready;

	List<Attendee> get(List<String> ids);

	public Page<Attendee> get(Pageable pageAble, List<String> eventIds);

	public Page<Attendee> get(Pageable pageAble, Criteria search);

	public Attendee edit(Attendee attendee) throws NotFoundException;

	public Attendee get(String id);

	public List<Attendee> get(String selector, String value);

	public Attendee edit(Attendee attendee, String id) throws NotFoundException;

	public List<Attendee> get(Map<String, Object> map);

	public SinchUser createSinchUser(String sinchAppId) throws ConstantNotExistsException;
}
