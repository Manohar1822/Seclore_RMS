package com.seclore.main.service;

import java.util.List;

import com.seclore.main.domain.UserDetails;

public interface UserDetailsServiceInterface {
	
		public boolean addNewUser(UserDetails user);

		public boolean updateUserDetails(UserDetails user);

		public boolean userLogin(int userId, String password);

		public UserDetails findUser(int userId);

		public List<UserDetails> getAllUsers();
		
		public boolean updatePassword(int userId, String password);
	
}