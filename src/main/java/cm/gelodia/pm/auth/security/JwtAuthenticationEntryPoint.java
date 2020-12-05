package cm.gelodia.pm.auth.security;

import cm.gelodia.pm.commons.exception.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}}", exception.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(401, LocalDateTime.now(), exception.getLocalizedMessage(), "uri="+ request.getServletPath());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(errorDetails);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(json);
    }
}
