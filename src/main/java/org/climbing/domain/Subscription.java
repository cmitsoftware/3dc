package org.climbing.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscription")
public class Subscription implements java.io.Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType. IDENTITY)
    private Integer id;

    @ManyToOne(cascade=CascadeType.ALL, optional=true, fetch = FetchType.EAGER)
    @JoinColumn(name="person_id", nullable=false)
    private Person person;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "reference_year")
    private Integer referenceYear;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getTypeName() { return typeName; }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Integer getReferenceYear() { return referenceYear; }

    public void setReferenceYear(Integer referenceYear) { this.referenceYear = referenceYear; }
}
