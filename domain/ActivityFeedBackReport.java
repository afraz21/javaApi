package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

public class ActivityFeedBackReport implements Serializable {

	public ActivityFeedBackReport(List<FeedBackReportResponse> report, long count) {
		super();
		this.report = report;
		this.count = count;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<FeedBackReportResponse> report;

	private long count;

	public List<FeedBackReportResponse> getReport() {
		return report;
	}

	public void setReport(List<FeedBackReportResponse> report) {
		this.report = report;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
