package com.devsuperior.dscommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.tests.UserDetailsFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.devsuperior.dscommerce.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private CustomUserUtil userUtil;
	
	private String existUsername, nonExistUsername;
	private User user;
	private List<UserDetailsProjection> userDetails;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existUsername = "maria@gmail.com";
		nonExistUsername = "user@gmail.com";
		
		user = UserFactory.createCustomClientUser(1L, existUsername);
		userDetails = UserDetailsFactory.createCustomAdminUser(existUsername);
		
		Mockito.when(repository.searchUserAndRolesByEmail(existUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(nonExistUsername)).thenReturn(new ArrayList<>());
		
		Mockito.when(repository.findByEmail(existUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByEmail(nonExistUsername)).thenReturn(Optional.empty());
	}
	
	@Test
	public void loadByUsernameShouldReturnUserDetailsWhenUserExist() {
		
		UserDetails result = service.loadUserByUsername(existUsername);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existUsername);
	}
	
	@Test
	public void loadByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistUsername);
		});
	}
	
	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(existUsername);
		
		User result = service.authenticated();
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existUsername);
	}
	
	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}
}
