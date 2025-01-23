package cat.institutmarianao.sailing.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Override
	public UserDetails loadUserByUsername(String username) {

		User user = userRepository.findById(username)
				.orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.UserService.user.not.found", new String[] { username }, LocaleContextHolder.getLocale())));

		return new UserDetailsImpl(user);
	}

}
