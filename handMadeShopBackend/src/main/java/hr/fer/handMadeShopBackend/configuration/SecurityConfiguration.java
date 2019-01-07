package hr.fer.handMadeShopBackend.configuration;

import hr.fer.handMadeShopBackend.Constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String REALM = "MY_REALM";

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

        String hash = passwordEncoder().encode("password");

        auth.inMemoryAuthentication().withUser("ivan").password(hash).roles(Constants.ROLE_USER, Constants.ROLE_ADMIN);
        auth.inMemoryAuthentication().withUser("martin").password(hash).roles(Constants.ROLE_USER, Constants.ROLE_ADMIN);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService());
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/hello").hasRole(Constants.ROLE_USER)
                .antMatchers("/hello/private").hasRole(Constants.ROLE_ADMIN)
                .antMatchers("/auth/register").permitAll()
                .antMatchers("/auth/login", "/auth/logout").authenticated()
                .antMatchers("/user/delete/**", "/user/update").authenticated()
                .antMatchers("/user/forbid/**").hasRole(Constants.ROLE_ADMIN)
                .antMatchers("/orders", "/orders/manage").hasRole(Constants.ROLE_ADMIN)
                .antMatchers("/orders/orderDecoration/**").authenticated()
                .antMatchers("/transactions/all").hasRole(Constants.ROLE_ADMIN)
                .antMatchers("/advertisement/publish").hasRole(Constants.ROLE_ADMIN)
                .antMatchers("/stories/recommend").authenticated()
                .antMatchers("/stories/publish", "/stories/manage").hasRole(Constants.ROLE_ADMIN)
                .antMatchers("/media/story/image/upload/**", "media/story/video/upload/**").authenticated()
                .antMatchers("/media/advertisement/image/upload/**").hasRole(Constants.ROLE_ADMIN)
                .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//We don't need sessions to be created.
    }

    @Bean
    public RestAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        final Properties users = new Properties();
        return new InMemoryUserDetailsManager(users);
    }
}
