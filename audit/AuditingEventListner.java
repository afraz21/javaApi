package org.iqvis.nvolv3.audit;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class AuditingEventListner implements ApplicationListener<BeforeConvertEvent<Object>> {

	@SuppressWarnings("unused")
	private AuditorAware<String> auditorAware;

	public AuditingEventListner(AuditorAware<String> auditorAware) {

		this.auditorAware = auditorAware;

	}

	public void onApplicationEvent(BeforeConvertEvent<Object> event) {

		Object obj = event.getSource();

		if (Audit.class.isAssignableFrom(obj.getClass())) {

			Audit entity = (Audit) obj;

			if (entity.isNew()) {

				entity.setCreatedDate(new DateTime());
			}

			entity.setLastModifiedDate(new DateTime());
		}
	}
}
