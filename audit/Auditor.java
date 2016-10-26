package org.iqvis.nvolv3.audit;

import org.springframework.data.domain.AuditorAware;

public class Auditor implements AuditorAware<String> {

	public String getCurrentAuditor() {
		// If you are using spring-security, you may get this from
		// SecurityContext.
		return "System";
	}
}
