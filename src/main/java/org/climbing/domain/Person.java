package org.climbing.domain;
// Generated 14-feb-2016 21.19.01 by Hibernate Tools 4.3.1.Final

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Person generated by hbm2java
 */
@Entity
@Table(name = "person")
public class Person implements java.io.Serializable {

	private Integer id;
	private User user;
	private String name;
	private String surname;
	private String address;
	private String city;
	private String phone;
	private String email;
	private String cf;
	private Date birthDate;
	private Date registrationDate;
	private Date subscriptionDate;
	private Date certificationDate;
	private Date affiliationDate;
	private Date freeEntryDate;
	private Boolean mailing;
	private Date creationDate;
	private Integer number;

	public Person() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "surname")
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "city")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "cf")
	public String getCf() {
		return this.cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "birth_date", length = 19)
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registration_date", length = 19)
	public Date getRegistrationDate() {
		return this.registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "subscription_date", length = 19)
	public Date getSubscriptionDate() {
		return this.subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "certification_date", length = 19)
	public Date getCertificationDate() {
		return this.certificationDate;
	}

	public void setCertificationDate(Date certificationDate) {
		this.certificationDate = certificationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "affiliation_date", length = 19)
	public Date getAffiliationDate() {
		return this.affiliationDate;
	}

	public void setAffiliationDate(Date affiliationDate) {
		this.affiliationDate = affiliationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "free_entry_date", length = 19)
	public Date getFreeEntryDate() {
		return this.freeEntryDate;
	}

	public void setFreeEntryDate(Date freeEntryDate) {
		this.freeEntryDate = freeEntryDate;
	}

	@Column(name = "mailing")
	public Boolean getMailing() {
		return mailing;
	}

	public void setMailing(Boolean mailing) {
		this.mailing = mailing;
	}	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", length = 19)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Column(name = "number")
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	@Transient
	public boolean registrationValid(){
		if(this.registrationDate != null) {
			Calendar startYear = Calendar.getInstance();
			startYear.set(Calendar.MONTH, 0);
			startYear.set(Calendar.DAY_OF_MONTH, 1);
			Calendar endYear = Calendar.getInstance();
			endYear.set(Calendar.MONTH, 11);
			endYear.set(Calendar.DAY_OF_MONTH, 31);
			if(startYear.getTime().before(this.registrationDate)
					&& endYear.getTime().after(this.registrationDate)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public boolean certificationValid(){
		if(this.certificationDate != null) {
			/*
			 * Modified 2017 12 01 
			 */
//			Calendar startYear = Calendar.getInstance();
//			startYear.set(Calendar.MONTH, 0);
//			startYear.set(Calendar.DAY_OF_MONTH, 1);
//			Calendar endYear = Calendar.getInstance();
//			endYear.set(Calendar.MONTH, 11);
//			endYear.set(Calendar.DAY_OF_MONTH, 31);
//			if(startYear.getTime().before(this.certificationDate)
//					&& endYear.getTime().after(this.certificationDate)) {
//				return true;
//			}
			Calendar cert = Calendar.getInstance();
			cert.setTime(this.certificationDate);
			cert.add(Calendar.DAY_OF_YEAR, 365);
			if(cert.after(Calendar.getInstance())) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public boolean subscriptionValid(){
		if(this.subscriptionDate != null) {
			Calendar startYear = Calendar.getInstance();
			startYear.set(Calendar.MONTH, 0);
			startYear.set(Calendar.DAY_OF_MONTH, 1);
			Calendar endYear = Calendar.getInstance();
			endYear.set(Calendar.MONTH, 11);
			endYear.set(Calendar.DAY_OF_MONTH, 31);
			if(startYear.getTime().before(this.subscriptionDate)
					&& endYear.getTime().after(this.subscriptionDate)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public boolean freeEntryAvailable(){
		if(this.freeEntryDate != null) {
			Calendar startYear = Calendar.getInstance();
			startYear.set(Calendar.MONTH, 0);
			startYear.set(Calendar.DAY_OF_MONTH, 1);
			Calendar endYear = Calendar.getInstance();
			endYear.set(Calendar.MONTH, 11);
			endYear.set(Calendar.DAY_OF_MONTH, 31);
			if(startYear.getTime().before(this.freeEntryDate)
					&& endYear.getTime().after(this.freeEntryDate)) {
				return false;
			}
		}
		return true;
	}
}