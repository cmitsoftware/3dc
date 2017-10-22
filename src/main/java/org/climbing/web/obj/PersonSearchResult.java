package org.climbing.web.obj;

import java.util.List;

public class PersonSearchResult {

	private Integer draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private String error;
	private List<PersonObj> data;
	
	public PersonSearchResult(){}

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<PersonObj> getData() {
		return data;
	}

	public void setData(List<PersonObj> data) {
		this.data = data;
	}
	
}
