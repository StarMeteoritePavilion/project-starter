package com.lcn29.starter.security.component.filter;

import com.lcn29.starter.security.component.service.JwtService;
import com.lcn29.starter.security.property.LcnSecurityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2019-12-17 14:05
 */

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationTokenFilter.class);

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtService jwtService;

    /**
     * the mark in the http header
     */
    private String httpHeaderMark;


    public JwtAuthorizationTokenFilter(LcnSecurityProperty property) {
        this.httpHeaderMark = property.getJwtHttpHeader();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authToken = httpServletRequest.getHeader(httpHeaderMark);
        String username = null;

        if (authToken != null) {
            username = jwtService.getAudienceFromToken(authToken);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // get the user info
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // validate token
            if (jwtService.validateToken(authToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
