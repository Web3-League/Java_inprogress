package irc_switch.config;

import org.springframework.security.config.Customizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/user/register/**", "/user/login/**", "/register/**", "/login/**", "/admin-credentials/**", "/admin-credentials/delete/**", "/admin/**", "/user/**", "/**", "/api/**").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers("/api/chat/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .cors(Customizer.withDefaults())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                
    }
}
