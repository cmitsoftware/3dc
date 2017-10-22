package org.climbing.security;

import java.util.Collection;
import java.util.List;

import org.climbing.domain.User;
import org.springframework.security.core.GrantedAuthority;

public class ClimbingUserDetails extends org.springframework.security.core.userdetails.User{

	private static final long serialVersionUID = 1L;
	
	/*
	 * 3d climbing user 
	 */
	private User user;
	
	public ClimbingUserDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}

	public ClimbingUserDetails(User user, List<GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
