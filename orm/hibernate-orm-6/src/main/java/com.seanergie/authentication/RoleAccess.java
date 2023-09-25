package com.seanergie.authentication;

import com.seanergie.persistence.ObjectWithUnid;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Immutable;

/**
 * Represents a Role granted to a User.
 */

@MappedSuperclass
@Access( AccessType.PROPERTY )
@Immutable
public class RoleAccess<
		TRoleAccess extends RoleAccess<TRoleAccess,TUser,TRole>,
		TUser extends UserWithRolesBase<TUser,TRole,TRoleAccess>,
		TRole extends Enum<TRole>
		> extends ObjectWithUnid {

	private TUser grantedTo;
	private TRole role;
	private Instant created;

	public RoleAccess() {
		super();
	}

	public RoleAccess(TUser grantedTo, TRole role) {
		this.grantedTo = grantedTo;
		this.role = role;
	}

	@Transient
	public TUser getGrantedTo() {
		return grantedTo;
	}
	public void setGrantedTo(TUser grantedTo) {
		this.grantedTo = grantedTo;
	}

	@Enumerated( EnumType.STRING )
	@Column( nullable = false, length = 100 )
	public TRole getRole() {
		return role;
	}
	public void setRole(TRole role) {
		this.role = role;
	}

	@Basic( optional = false )
	public Instant getCreated() {
		return created;
	}
	public void setCreated(Instant created) {
		this.created = created;
	}

	@PrePersist
	protected void onCreate() {
		setCreated( Instant.now() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( role );
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj
				|| obj instanceof RoleAccess<?,?,?> other
				&& role == other.role
				&& Objects.equals( grantedTo, other.grantedTo );
	}

	@Override
	public String toString() {
		return new ToStringBuilder( this )
				.append( "grantedTo", grantedTo )
				.append( "role", role )
				.append( "created", created )
				.toString();
	}
}