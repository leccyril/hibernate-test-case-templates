package com.seanergie.authentication;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@MappedSuperclass
@Access( AccessType.PROPERTY )
public class WebUser<
		TUser extends WebUser<TUser,TRole,TRoleAccess>,
		TRole extends Enum<TRole>,
		TRoleAccess extends RoleAccess<TRoleAccess,TUser,TRole>>
		extends WebUserWithRolesBase<TUser,TRole,TRoleAccess> {

	@OneToMany( cascade = CascadeType.REMOVE, mappedBy = "grantedTo" )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE )
	@Override
	@SuppressWarnings( "EmptyMethod" )
	public Set<TRoleAccess> getRoleAccesses() {
		return super.getRoleAccesses();
	}
}