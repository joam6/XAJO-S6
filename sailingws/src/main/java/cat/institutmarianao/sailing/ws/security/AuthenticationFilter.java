package cat.institutmarianao.sailing.ws.security;

import java.io.IOException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.response.authorization.header}")
	private String headerAuthorization;

	@Value("${jwt.response.authorization.value}")
	private String bearer;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String jwtToken = extractToken(request);

		if (!Strings.isEmpty(jwtToken)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtils
					.getAuthentication(jwtToken);

			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}

		chain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(headerAuthorization);
		if (StringUtils.hasText(bearerToken) && bearerToken.contains(bearer)) {
			return bearerToken.replaceFirst(bearer, "").trim();
		}
		return null;
	}

}