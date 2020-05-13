package org.climbing.web.obj;

import org.climbing.domain.Person;
import org.climbing.util.Constants;

public class PersonObj {
	
	private Integer id;
	private Integer number;
	private String name;
	private String surname;
	private String email;
	private String registrationDate;
	//private String subscriptionDate;
	private String certificationDate;
	private String freeEntryDate;
	private Boolean registrationValid;
	private Boolean subscriptionValid;
	private Boolean certificationValid;
	private Boolean freeEntryAvailable;
	
	public PersonObj(Person p) {
		this.id = p.getId();
		this.number = p.getNumber();
		this.name = p.getName();
		this.surname = p.getSurname();
		this.email = p.getEmail();
		if(p.getRegistrationDate() != null) {
			this.registrationDate  = Constants.timeFormat().format(p.getRegistrationDate());
		}
		//if(p.getSubscriptionDate() != null) {
		//	this.subscriptionDate  = Constants.timeFormat().format(p.getSubscriptionDate());
		//}
		if(p.getCertificationDate() != null) {
			this.certificationDate  = Constants.timeFormat().format(p.getCertificationDate());
		}
		if(p.getFreeEntryDate() != null) {
			this.freeEntryDate  = Constants.timeFormat().format(p.getFreeEntryDate());
		}
		this.registrationValid = p.registrationValid();
		this.subscriptionValid = p.subscriptionValid();
		this.certificationValid = p.certificationValid();
		this.freeEntryAvailable = p.freeEntryAvailable();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	//public String getSubscriptionDate() {
	//	return subscriptionDate;
	//}
	//public void setSubscriptionDate(String subscriptionDate) {
	//	this.subscriptionDate = subscriptionDate;
	//}
	public String getCertificationDate() {
		return certificationDate;
	}
	public void setCertificationDate(String certificationDate) {
		this.certificationDate = certificationDate;
	}
	public String getFreeEntryDate() {
		return freeEntryDate;
	}
	public void setFreeEntryDate(String freeEntryDate) {
		this.freeEntryDate = freeEntryDate;
	}
	public Boolean getRegistrationValid() {
		return registrationValid;
	}
	public void setRegistrationValid(Boolean registrationValid) {
		this.registrationValid = registrationValid;
	}
	public Boolean getSubscriptionValid() {
		return subscriptionValid;
	}
	public void setSubscriptionValid(Boolean subscriptionValid) {
		this.subscriptionValid = subscriptionValid;
	}
	public Boolean getCertificationValid() {
		return certificationValid;
	}
	public void setCertificationValid(Boolean certificationValid) {
		this.certificationValid = certificationValid;
	}
	public Boolean getFreeEntryAvailable() {
		return freeEntryAvailable;
	}
	public void setFreeEntryAvailable(Boolean freeEntryAvailable) {
		this.freeEntryAvailable = freeEntryAvailable;
	}
	
}
