package com.api.lista_de_compras.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    // @Autowired
    // TokenService tokenService;
    // @Autowired
    // UserRegisterRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, RuntimeException {
        // var token = this.recoverToken(request);
        // var login = tokenService.validateToken(token);

        // if(login != null){
        //     User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found"));
        //     var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        //     var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        //     SecurityContextHolder.getContext().setAuthentication(authentication);
        //     System.out.println("Entrou aqui XYZ");
        // }
        filterChain.doFilter(request, response);
    }

    // private String recoverToken(HttpServletRequest request){
    //     var authHeader = request.getHeader("Authorization");
    //     if(authHeader == null) return null;
    //     System.out.println("ABCD entrou aqui");
    //     return authHeader.replace("Bearer ", "");
    // }
}