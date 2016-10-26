package org.iqvis.nvolv3.search;

import java.util.List;

public class Query {

	List<String> select;

	List<Where> where;

	List<OrderBy> orderBy;

	Integer pageNumber;

	Integer pageSize;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<String> getSelect() {
		return select;
	}

	public void setSelect(List<String> select) {
		this.select = select;
	}

	public List<Where> getWhere() {
		return where;
	}

	public void setWhere(List<Where> where) {
		this.where = where;
	}

	public List<OrderBy> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
	}

}
