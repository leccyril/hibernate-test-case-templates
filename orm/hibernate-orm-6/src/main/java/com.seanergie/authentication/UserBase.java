package com.seanergie.authentication;

import com.seanergie.persistence.ObjectWithUnidAndVersion;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.lang3.RandomStringUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

/**
 * This is a base class that holds a login, password, ...
 * It can be used as a base class of a Mail user, a Web user, ...
 */
@MappedSuperclass
@Table( indexes = @Index( name = "user_status_index", columnList = "status" ) )
@Access( AccessType.PROPERTY )
public class UserBase extends ObjectWithUnidAndVersion {

	private Instant created;
	private String login;
	private String password;
	private String salt;
	private String hashAlgorithm;
	private Instant lastPasswordChange;
	private UserStatus status = UserStatus.ACTIVE;

	@Basic( optional = false )
	public Instant getCreated() {
		if( created == null ){
			created = Instant.now();
		}

		return created;
	}
	public void setCreated(Instant created) {
		this.created = created;
	}

	/**
	 * The login used to log on.
	 */
	@Basic
	@Column( unique = true, nullable = false, length = 60 )
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login == null ? "" : login.trim().toLowerCase( Locale.ENGLISH );
	}

	@Basic( optional = false )
	public String getPassword() {
		return password;
	}
	/**
	 * Only for hibernate. Do not use.
	 * Use changePassword instead.
	 */
	@Deprecated
	protected void setPassword(String password) { // NOSONAR
		this.password = password;
	}

	@Basic( optional = false )
	
	public Instant getLastPasswordChange() {
		if( lastPasswordChange == null ){
			lastPasswordChange = Instant.now();
		}

		return lastPasswordChange;
	}
	/**
	 * Only for hibernate. Do not use.
	 * Use changePassword instead.
	 */
	@Deprecated
	protected void setLastPasswordChange(Instant lastPasswordChange) { // NOSONAR
		this.lastPasswordChange = lastPasswordChange;
	}

	@Transient
	public boolean isActivated() {
		return status == UserStatus.ACTIVE;
	}

	@Enumerated( EnumType.STRING )
	@Column( length = 25, nullable = false )
	
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	@Basic
	@Column( length = 16 )
	public String getSalt() {
		if( salt == null ){
			salt = RandomStringUtils.randomAlphanumeric( 16 ); // NOSONAR
		}

		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column( length = 15 )
	public String getHashAlgorithm() {
		return hashAlgorithm;
	}
	public void setHashAlgorithm(String hashFunction) {
		this.hashAlgorithm = hashFunction;
	}


	@Transient
	public static String getRandomPassword() {
		final List<CharacterRule> rules = Arrays.asList(
				// at least one upper-case character
				new CharacterRule( EnglishCharacterData.UpperCase, 1 ),

				// at least one lower-case character
				new CharacterRule( EnglishCharacterData.LowerCase, 1 ),

				// at least one digit character
				new CharacterRule( EnglishCharacterData.Digit, 1 ) );

		var generator = new PasswordGenerator();

		// Generated password is 12 characters long, which complies with policy
		return generator.generatePassword( 12, rules ); // NOSONAR
	}


	@Transient
	public String getFullID() {
		return login + " (" + getUnid() + ')';
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( login );
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj
				|| obj instanceof UserBase toCompare
				&& Objects.equals( getLogin(), toCompare.getLogin() )
				&& Objects.equals( getPassword(), toCompare.getPassword() )
				&& getStatus() == toCompare.getStatus()
				&& Objects.equals( salt, toCompare.salt )
				&& Objects.equals( getHashAlgorithm(), toCompare.getHashAlgorithm() );
	}
}