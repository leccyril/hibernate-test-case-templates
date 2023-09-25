package com.seanergie.authentication;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

@MappedSuperclass
@Access( AccessType.PROPERTY )
public class UserWithRolesBase<
		TUser extends UserWithRolesBase<TUser,TRole,TRoleAccess>,
		TRole extends Enum<TRole>,
		TRoleAccess extends RoleAccess<TRoleAccess,TUser,TRole>>
		extends UserBase implements Comparable<TUser> {

	private String firstName;
	private String lastName;

	private Instant lastLogin;
	private Instant lastFailedLogin;

	private Set<TRoleAccess> roleAccesses;

	@Basic
	@Column( length = 30 )
	//@SearchField
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column( length = 30 )
	//@SearchField
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Transient
	public String getFullName() {
		if( StringUtils.isBlank( firstName ) && StringUtils.isBlank( lastName ) ){
			return getLogin();
		}else{
			return StringUtils.trimToEmpty( StringUtils.trimToEmpty( firstName ) + " " + StringUtils.trimToEmpty( lastName ) );
		}
	}

	@Basic
	public Instant getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Instant lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Basic
	public Instant getLastFailedLogin() {
		return lastFailedLogin;
	}
	public void setLastFailedLogin(Instant lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}

	@Transient
	public Instant getLastLoginAttempt() {
		if( lastLogin == null ){
			return lastFailedLogin;
		}
		if( lastFailedLogin == null ){
			return lastLogin;
		}

		return lastLogin.isAfter( lastFailedLogin ) ? lastLogin : lastFailedLogin;
	}

	@Transient
	public boolean isLastLoginFailed() {
		return lastFailedLogin != null && (lastLogin == null || lastFailedLogin.isAfter( lastLogin ));
	}

	@Transient
	public Set<TRoleAccess> getRoleAccesses() {
		if( roleAccesses == null ){
			roleAccesses = new HashSet<>();
		}

		return roleAccesses;
	}
	public void setRoleAccesses(Set<TRoleAccess> roleAccesses) {
		this.roleAccesses = roleAccesses;
	}

	@Transient
	public List<TRoleAccess> getRoleAccessesList() {
		return List.copyOf( getRoleAccesses() );
	}

	@Transient
	public Set<TRole> getRoles() {
		var toReturn = new HashSet<TRole>( getRoleAccesses().size() );
		for( var roleAccess : getRoleAccesses() ){
			toReturn.add( roleAccess.getRole() );
		}

		return toReturn;
	}

	@SafeVarargs
	public final Stream<TRoleAccess> getFilteredRoleAccessesStream(final TRole... matchingRolesFilter) {
		return getRoleAccesses()
				.stream()
				.filter( roleAccess -> {
					for( var role : matchingRolesFilter ){
						if( role == roleAccess.getRole() ){ // NOSONAR. Ok, comparing enums.
							return true;
						}
					}

					return false;
				} );
	}

	protected Stream<TRoleAccess> getFilteredRoleAccessesStream(final String... matchingRoleNamesFilter) {
		return getRoleAccesses()
				.stream()
				.filter( roleAccess -> {
					for( String roleName : matchingRoleNamesFilter ){
						if( roleAccess.getRole().name().equals( roleName ) ){
							return true;
						}
					}

					return false;
				} );
	}

	@Transient
	public List<TRole> getRolesAsList() {
		var toReturn = new ArrayList<TRole>( getRoleAccesses().size() );
		for( var roleAccess : getRoleAccesses() ){
			toReturn.add( roleAccess.getRole() );
		}

		return toReturn;
	}

	public boolean isHasRole(TRole role) {
		if( role == null ){
			return false;
		}

		for( var roleAccess : getRoleAccesses() ){
			if( roleAccess.getRole() == role ){ // NOSONAR - == ok as those are enums.
				return true;
			}
		}

		return false;
	}

	@Deprecated
	protected boolean isHasRole(String roleName) { // NOSONAR
		if( StringUtils.isBlank( roleName ) ){
			return false;
		}

		for( var roleAccess : getRoleAccesses() ){
			if( roleName.equals( roleAccess.getRole().name() ) ){
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings( { "unchecked", "BooleanMethodIsAlwaysInverted" } )
	public boolean isHasARole(final TRole... roles) {
		if( roles == null ){
			return false;
		}

		for( var roleAccess : getRoleAccesses() ){
			for( var role : roles ){
				if( role == roleAccess.getRole() ){ // NOSONAR. Ok, comparing enums.
					return true;
				}
			}
		}

		return false;
	}

	@Deprecated
	public boolean isHasARole(final String... roleNames) { // NOSONAR
		if( roleNames == null ){
			return false;
		}

		for( var roleAccess : getRoleAccesses() ){
			for( String roleName : roleNames ){
				if( roleAccess.getRole().name().equals( roleName ) ){
					return true;
				}
			}
		}

		return false;
	}

	public boolean isHasARole(final Collection<TRole> roles) {
		if( roles == null ){
			return false;
		}

		for( var roleAccess : getRoleAccesses() ){
			for( var role : roles ){
				if( role == roleAccess.getRole() ){ // NOSONAR - == ok as those are enums.
					return true;
				}
			}
		}

		return false;
	}

	public boolean isHasARole(final Set<TRole> roles) {
		if( roles == null ){
			return false;
		}

		for( var roleAccess : getRoleAccesses() ){
			if( roles.contains( roleAccess.getRole() ) ){
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return getLogin();
	}

	@Override
	@Transient
	public String getFullID() {
		return getFullName() + " [" + getLogin() + " / " + getUnid() + ']';
	}

	@Override
	public int compareTo(TUser other) {
		return new CompareToBuilder()
				.append( StringUtils.lowerCase( this.lastName ), StringUtils.lowerCase( other.getLastName() ) )
				.append( StringUtils.lowerCase( this.firstName ), StringUtils.lowerCase( other.getFirstName() ) )
				.append( StringUtils.lowerCase( this.getLogin() ), StringUtils.lowerCase( other.getLogin() ) )
				.toComparison();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getLogin() );
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}