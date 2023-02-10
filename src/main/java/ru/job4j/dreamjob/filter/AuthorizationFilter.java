package ru.job4j.dreamjob.filter;

import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;
import ru.job4j.dreamjob.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        request.setAttribute("user", user);

        chain.doFilter(request, response);
    }
}
