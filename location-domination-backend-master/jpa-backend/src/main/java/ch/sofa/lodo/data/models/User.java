package ch.sofa.lodo.data.models;

import ch.sofa.lodo.data.constants.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
@Entity
@Table(name = "user")
public class User extends SuperEntity implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "user_name", length = 20)
	private String username = "";

	@Column(name = "password_hash", length = 100)
	private String passwordHash = "";

	@Column(name = "mobile_number", length = 20)
	private String mobileNumber = "";

	@Column(name = "register_datetime")
	private LocalDateTime registerDateTime;

	@Column(name = "count_password_reset")
	private int countPasswordReset = 0;

	@Column(name = "count_login")
	private int countLogin = 0;

	@Column(name = "authenticated")
	private boolean authenticated = false;

	@Column(name = "blocked")
	private boolean blocked;

	@Column(name = "receive_price_per_post")
	private boolean receivePricePerPost;

	@Column(name = "first_name", length = 50)
	private String firstName = "";

	@Column(name = "last_name", length = 50)
	private String lastName = "";

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "display_image")
	private byte[] displayImage;

	@Column(name = "display_name", length = 20)
	private String displayName = "";

	@Column(name = "is_verified")
	private Boolean isVerified;

	@Column(name = "is_admin")
	private Boolean isAdmin;

	@Column(name = "deleted")
	private boolean deleted = false;

	@Column(name = "authorization_code")
	private String authorizationCode;

	@Column(name = "firebase_user_device_token", length = 200)
	private String firebaseUserDeviceToken;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type", length = 12, nullable = false)
	private UserType userType;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;
	@Transient
	private String passwordConfirm = "";

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Transient
	public String getUserFullName() {
		return String.join(" ", firstName, lastName);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return passwordHash;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isVerified == null ? false : isVerified;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !blocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return !blocked;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public LocalDateTime getRegisterDateTime() {
		return registerDateTime;
	}

	public void setRegisterDateTime(LocalDateTime registerDateTime) {
		this.registerDateTime = registerDateTime;
	}

	public int getCountPasswordReset() {
		return countPasswordReset;
	}

	public void setCountPasswordReset(int countPasswordReset) {
		this.countPasswordReset = countPasswordReset;
	}

	public int getCountLogin() {
		return countLogin;
	}

	public void setCountLogin(int countLogin) {
		this.countLogin = countLogin;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isReceivePricePerPost() {
		return receivePricePerPost;
	}

	public void setReceivePricePerPost(boolean receivePricePerPost) {
		this.receivePricePerPost = receivePricePerPost;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public byte[] getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(byte[] displayImage) {
		this.displayImage = displayImage;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getFirebaseUserDeviceToken() {
		return firebaseUserDeviceToken;
	}

	public void setFirebaseUserDeviceToken(String firebaseUserDeviceToken) {
		this.firebaseUserDeviceToken = firebaseUserDeviceToken;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
