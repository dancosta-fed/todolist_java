package br.com.crznews.todolist.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.crznews.todolist.user.IUserRepository;
import br.com.crznews.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class FilterTaskAuth  extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
      
        String servletPath = request.getServletPath();

        if (!Objects.equals(servletPath, "/tasks/")) {
          filterChain.doFilter(request, response);
          return;
        }

        String authorization = request.getHeader("Authorization");

        String authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

        String authString = new String(authDecoded);

        String[] credentials = authString.split(":");
        String userName = credentials[0];
        String password = credentials[1];

        UserModel user = this.userRepository.findByUserName(userName);

        if (user == null) {
          response.sendError(401);
          return;
        }

        Result passwordVerification = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        if (!passwordVerification.verified) {
          response.sendError(401);
          return;
        }

        filterChain.doFilter(request, response);
  }
}
