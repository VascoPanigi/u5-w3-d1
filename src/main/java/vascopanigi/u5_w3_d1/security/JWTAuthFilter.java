package vascopanigi.u5_w3_d1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import vascopanigi.u5_w3_d1.entities.Employee;
import vascopanigi.u5_w3_d1.services.EmployeeService;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTTools jwtTools;

	@Autowired
	private EmployeeService employeeService;

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
		String employeeId = jwtTools.extractIdFromToken(accessToken);
		Employee currentEmployee = employeeService.findById(UUID.fromString(employeeId));

		Authentication authentication = new UsernamePasswordAuthenticationToken(currentEmployee, null, currentEmployee.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
