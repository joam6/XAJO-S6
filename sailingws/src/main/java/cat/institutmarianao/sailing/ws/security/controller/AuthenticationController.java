package cat.institutmarianao.sailing.ws.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.exception.ForbiddenException;
import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.repository.UserRepository;
import cat.institutmarianao.sailing.ws.security.JwtUtils;
import cat.institutmarianao.sailing.ws.security.model.AuthCredentials;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "Authentication API")

@RestController
public class AuthenticationController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Value("${jwt.response.authorization.header}")
	private String headerAuthorization;

	@Value("${jwt.response.authorization.value}")
	private String bearer;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<User> createAuthenticationToken(@RequestBody AuthCredentials authenticationRequest) {
		User user = userRepository.findById(authenticationRequest.getUsername()).orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", new String[] { "User", authenticationRequest.getUsername() }, LocaleContextHolder.getLocale())));
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			
			final String token = jwtUtils.generateToken(authentication.getName(), authentication.getAuthorities());
			
			//return ResponseEntity.ok().body(null).header(headerAuthorization, String.format("%s %s", bearer, token)).build();
			return ResponseEntity.ok().header(headerAuthorization, String.format("%s %s", bearer, token)).body(user);
		} catch (DisabledException e) {
			//return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(messageSource.getMessage("error.Authentication.user.disabled", null, LocaleContextHolder.getLocale()));
			throw new ForbiddenException(messageSource.getMessage("error.Authentication.user.disabled", null, LocaleContextHolder.getLocale()));
		} catch (BadCredentialsException e) {
			//return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(messageSource.getMessage("error.Authentication.bad.credentials", null, LocaleContextHolder.getLocale()));
			throw new ForbiddenException(messageSource.getMessage("error.Authentication.bad.credentials", null, LocaleContextHolder.getLocale()));
		}
	}

}