package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.domain.SystemConstant;
import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.stereotype.Service;

@Service(Constants.SERVICE_SYSTEMT_CONSTANT)
public interface SystemConstantService {

	/**
	 * @param constantName name of constant in database document 
	 * @return SystemConstant
	 * @throws SystemConstant
	 * */
	public SystemConstant get(String constantName) throws ConstantNotExistsException;
}
