package org.ivandzf.microservice.apigateway.security.oauth2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * example-microservice
 *
 * @author Divananda Zikry Fadilla (27 August 2018)
 * Email: divanandazf@gmail.com
 * <p>
 * Documentation here !!
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final DefaultTokenServices tokenServices;

    public ResourceServerConfig(@Qualifier("redisTokenStore") TokenStore tokenStore,
                                @Qualifier("customTokenService") DefaultTokenServices tokenServices) {
        this.tokenStore = tokenStore;
        this.tokenServices = tokenServices;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId("API-GATEWAY")
                .tokenStore(tokenStore)
                .tokenServices(tokenServices)
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/public/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**", "/configuration/**").permitAll()
                .antMatchers("/api/**/v2/api-docs").permitAll()
                .antMatchers("/actuator/**")
                .permitAll()
                .antMatchers("/api/**")
                .authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
