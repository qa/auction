package org.jboss.lectures.auction.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AuditUser implements Serializable {

	private static final long serialVersionUID = 6738831612830739013L;
	private String email;
	private Long id;
	private Timestamp eventTime;

	public AuditUser() {
		this.email = null;
	}

	public AuditUser(AuditUser user) {
		this.email = user.getEmail();
	}

	public AuditUser(String email) {
		this.email = email;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column()
	public Timestamp getEventTime() {
		return eventTime;
	}

	public void setEventTime(Timestamp eventTime) {
		this.eventTime = eventTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AuditUser))
			return false;

		AuditUser other = (AuditUser) obj;
		if (getEmail() == null) {
			if (other.getEmail() != null)
				return false;
		} else if (!getEmail().equals(other.getEmail())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UserAudit [name=" + email + "," + eventTime + "]";
	}

}
