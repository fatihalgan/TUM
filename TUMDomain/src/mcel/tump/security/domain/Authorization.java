package mcel.tump.security.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.acegisecurity.GrantedAuthority;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Authorization implements Serializable, Comparable<Authorization>,
		GrantedAuthority, SelectableDataItem, Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3796092143691297079L;
	private Long id;
	private String authorizationName;
	private String authorizationURI;

	private Set<Role> authorizedRoles = new TreeSet<Role>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorizationName() {
		return authorizationName;
	}

	public void setAuthorizationName(String authorizationName) {
		this.authorizationName = authorizationName;
	}

	public String getAuthorizationURI() {
		return authorizationURI;
	}

	public void setAuthorizationURI(String authorizationURI) {
		this.authorizationURI = authorizationURI;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Authorization))
			return false;
		Authorization castOther = (Authorization) other;
		return new EqualsBuilder().append(authorizationURI,
				castOther.authorizationURI).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(2076299639, -815969171).append(
				authorizationURI).toHashCode();
	}

	@Override
	public int compareTo(Authorization o) {
		return getAuthorizationName().compareTo(o.getAuthorizationName());
	}

	public Set<Role> getAuthorizedRoles() {
		return authorizedRoles;
	}

	public void setAuthorizedRoles(Set<Role> authorizedRoles) {
		this.authorizedRoles = authorizedRoles;
	}

	public void addRole(Role role) {
		getAuthorizedRoles().add(role);
		role.getRoleAuthorizations().add(this);
	}

	public void removeRole(Role role) {
		getAuthorizedRoles().remove(role);
		role.getRoleAuthorizations().remove(this);
	}

	public String getAuthority() {
		return getAuthorizationURI();
	}

	@Override
	public String toString() {
		return getAuthorizationName();
	}

	public void removeUnauthorizedRoles(List<Role> selectedRoles) {
		Role[] currentRoles = getAuthorizedRoles().toArray(new Role[getAuthorizedRoles().size()]);
		for(Role r : currentRoles) {
			if(!selectedRoles.contains(r)) removeRole(r);
		}
	}

	public String getAuditMessage() {
		return "";
	}

	public void setAuditMessage(String message) {

	}
}
