package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.ForbiddenException;
import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.repository.UserRepository;
import cat.institutmarianao.sailing.ws.security.JwtUtils;
import cat.institutmarianao.sailing.ws.service.UserService;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserUpdate;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
	public List<User> findAll(Role[] roles, String fullName) {
		if (!jwtUtils.isAdmin()) 
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.users.find", null, LocaleContextHolder.getLocale()));
		
		return userRepository.findAllByFilters(roles, fullName);
	}

	@Override
	public User getByUsername(@NotBlank String username) {
		if (!(jwtUtils.isAdmin() || jwtUtils.isAuthUser(username))) 
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.users.get", null, LocaleContextHolder.getLocale()));
		
		return userRepository.findById(username).orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", new String[] { "User", username }, LocaleContextHolder.getLocale())));
	}

	@Override
	public boolean existsById(@NotBlank String username) {
		return userRepository.existsById(username);
	}

	@Override
	@Validated(OnUserCreate.class)
	public User save(@NotNull @Valid User user) {
		if (!(jwtUtils.isAdmin() || user.getRole().equals(Role.CLIENT))) 
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.users.create", null, LocaleContextHolder.getLocale()));

		if (userRepository.existsById(user.getUsername())) 
			throw new ValidationException (messageSource.getMessage("error.UserService.username.exists", new String[] { user.getUsername() }, LocaleContextHolder.getLocale()));
		
		return userRepository.saveAndFlush(user);
	}

	@Override
	@Validated(OnUserUpdate.class)
	public User update(@NotNull @Valid User user) {
		if (!(jwtUtils.isAdmin() || jwtUtils.isAuthUser(user.getUsername()))) 
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.users.update", null, LocaleContextHolder.getLocale()));
		
		User dbUser = getByUsername(user.getUsername());

		if (user.getPassword() != null) {
			dbUser.setPassword(user.getPassword());
		}

		if (user instanceof Client client && dbUser instanceof Client dbClient) {
			dbClient.setFullName(client.getFullName());
			dbClient.setPhone(client.getPhone());
		}

		return userRepository.saveAndFlush(dbUser);
	}

	@Override
	public void deleteByUsername(@NotBlank String username) {
		if (!jwtUtils.isAdmin()) throw new ForbiddenException(messageSource.getMessage("error.Forbidden.users.delete", null, LocaleContextHolder.getLocale()));
		
		if (!userRepository.existsById(username))
			throw new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", new String[] { "User", username }, LocaleContextHolder.getLocale()));
		
		userRepository.deleteById(username);
	}
	
	
}
