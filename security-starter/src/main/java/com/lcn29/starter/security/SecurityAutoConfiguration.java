package com.lcn29.starter.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.lcn29.kit.util.StringUtil;
import com.lcn29.starter.security.component.auth.JwtAccessDeniedHandler;
import com.lcn29.starter.security.component.auth.JwtAuthenticationEntryPoint;
import com.lcn29.starter.security.component.filter.JwtAuthorizationTokenFilter;
import com.lcn29.starter.security.component.service.JwtService;
import com.lcn29.starter.security.property.LcnSecurityProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <pre>
 * security by token
 * </pre>
 *
 * @author LCN
 * @date 2020-02-22 17:33
 */
@ConditionalOnBean(UserDetailsService.class)
@ConditionalOnProperty(value = "app.security.enable", havingValue = "true")
@EnableConfigurationProperties(LcnSecurityProperty.class)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    private final static String URL_SEPARATOR = ",";

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * security config
     */
    @Resource
    private LcnSecurityProperty securityProperty;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //  With this configured, the encrypted password in the database
        //  is automatically compared to the password entered by the user
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // base jwt, csrf no need
        httpSecurity.csrf().disable();

        // the handler to solve token authenticated fail
        httpSecurity.exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint).and();

        //base token , session is not need
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        // request of preflight can access straight
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
            httpSecurity.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        // the url under matchers can access the service login without cross the filter
        String matcherString = securityProperty.getAntMatchers();
        if (StringUtil.isNotBlack(matcherString)) {
            String[] matchers = StringUtil.trimAllWhitespace(matcherString).split(URL_SEPARATOR);
            expressionInterceptUrlRegistry = expressionInterceptUrlRegistry.antMatchers(matchers).permitAll();
        }

        // other request must authenticated
        expressionInterceptUrlRegistry.anyRequest().authenticated();

        // before access the service logic, must can cross the filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // Disable Caching
        httpSecurity.headers().cacheControl();
    }

    /**
     * password encoder
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    /**
     * verify manager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * unauthorized handler
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint createAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    /**
     * no permission handler
     * @return
     */
    @Bean
    public AccessDeniedHandler createAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    /**
     * url TokenFilter
     *
     * @return
     * @throws Exception
     */
    @Bean
    public JwtAuthorizationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthorizationTokenFilter(securityProperty);
    }

    @Bean
    public JwtService createJwtService() throws Exception{
        Algorithm algorithm;
        switch (securityProperty.getTokenAlgorithmWay()) {
            case 1:
            case 2:
                algorithm = createJwtServiceByString(securityProperty.getPublicKey(), securityProperty.getPrivateKey());
                break;
            case 3:
                algorithm = createJwtServiceByFile(securityProperty.getPublicKey(), securityProperty.getPrivateKey());
                break;
            default:
                throw new UnsupportedOperationException("this way can't create JwtService");
        }
        JwtService service = new JwtService();
        service.setAlgorithm(algorithm);
        service.setIssuer(securityProperty.getIssuer());
        service.setSubject(securityProperty.getSubject());
        service.setTokenExpTime(securityProperty.getTokenExpTime());
        return service.build();
    }

    /**
     * generate Algorithm by public key String and private key String
     * @param publicKeyStr
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    private Algorithm createJwtServiceByString(String publicKeyStr, String privateKeyStr) throws Exception {

        // make the str to key object
        Base64.Decoder decoder = Base64.getDecoder();
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(decoder.decode(publicKeyStr));
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decoder.decode(privateKeyStr));
        KeyFactory keyFactory  = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(bobPubKeySpec);
        RSAPrivateKey privateKey  = (RSAPrivateKey)keyFactory.generatePrivate(priPKCS8);
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        return algorithm;
    }

    /**
     * generate Algorithm by public key file and private key file
     * @param publicKeyPath
     * @param privateKeyPath
     * @return
     */
    private Algorithm createJwtServiceByFile(String publicKeyPath, String privateKeyPath) throws Exception {
        // TODO 待实现
        return null;
    }


}
