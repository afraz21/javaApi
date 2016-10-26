package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.dao.SystemConstantDao;
import org.iqvis.nvolv3.domain.SystemConstant;
import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(Constants.SERVICE_SYSTEMT_CONSTANT)
@Transactional
public class SystemConstantServiceImpl implements SystemConstantService {
	
	@Autowired 
	private String systemtExecutionEnvironment;
	
	@Autowired 
	private SystemConstantDao systemConstantDao; 
	
	public SystemConstant get(String constantName) throws ConstantNotExistsException {

		return systemConstantDao.get(systemtExecutionEnvironment, constantName);
	}

}
