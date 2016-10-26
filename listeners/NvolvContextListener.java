package org.iqvis.nvolv3.listeners;

import org.iqvis.nvolv3.exceptionHandler.ConstantNotExistsException;
import org.iqvis.nvolv3.service.ConstantFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NvolvContextListener implements ApplicationListener<ApplicationEvent> {

	@Autowired
	private ConstantFactory constantFactory;

	public void onApplicationEvent(ApplicationEvent e) {

		if (e.getClass().equals(ContextRefreshedEvent.class)) {

			ContextRefreshedEvent context = (ContextRefreshedEvent) e;

			try {
				constantFactory.fillConstats();
			}
			catch (ConstantNotExistsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

}
