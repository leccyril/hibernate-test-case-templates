package com.seanergie.authentication;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
@Access( AccessType.PROPERTY )
public class WebUserWithRolesBase<TUser extends WebUserWithRolesBase<TUser,TRole,TRoleAccess>,
		TRole extends Enum<TRole>,
		TRoleAccess extends RoleAccess<TRoleAccess,TUser,TRole>> extends UserWithRolesBase<TUser,TRole,TRoleAccess>  {

	private String socialLoginProvider;
	private String socialLoginUserId;

	@Basic
	public String getSocialLoginUserId() {
		return socialLoginUserId;
	}
	public void setSocialLoginUserId(String socialLoginUserId) {
		this.socialLoginUserId = socialLoginUserId;
	}

	@Basic
	public String getSocialLoginProvider() {
		return socialLoginProvider;
	}
	public void setSocialLoginProvider(String socialLoginProvider) {
		this.socialLoginProvider = socialLoginProvider;
	}

	@Override
	@SuppressWarnings( "RedundantMethodOverride" )
	public int hashCode() {
		return Objects.hashCode( getLogin() );
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}