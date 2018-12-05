package springmvc.demo.hooks;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import springmvc.demo.services.authentication.TokenAuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) servletRequest);

        final String expiredMsg = (String) servletRequest.getAttribute("expired");

//        final String msg = (expiredMsg == null)? expiredMsg: "Unauthorized";

        System.out.println("expiredMsg: " + expiredMsg);
        if(expiredMsg != null) {

//            servletResponse.
            HttpServletResponse resp = (HttpServletResponse) servletResponse;

            resp.setContentType("application/json");

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JSONObject response = new JSONObject();

            response.put("message", expiredMsg);

//            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, response.toString());
            resp.getOutputStream().println(response.toString());
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
