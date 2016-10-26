package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.utils.SystemConstantEnum;

public interface ConstantFactory {
	
	Object getValue(SystemConstantEnum constant) throws ConstantNotExistsException;
	
	void fillConstats() throws ConstantNotExistsException;
}
