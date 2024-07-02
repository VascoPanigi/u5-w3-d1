package vascopanigi.u5_w3_d1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import vascopanigi.u5_w3_d1.exceptions.UnauthorizedException;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTTools jwtTools;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please insert token in the request's header.");
			return;
		}
		String accessToken = authHeader.substring(7);

		try {
			jwtTools.verifyToken(accessToken);
		} catch (InvalidCsrfTokenException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not valid");
			return;
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
