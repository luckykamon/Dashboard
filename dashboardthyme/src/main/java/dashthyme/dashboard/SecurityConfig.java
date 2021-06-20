package dashthyme.dashboard;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RestController;


@Configuration
@EnableWebSecurity
@RestController
@SpringBootApplication
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .and()
                .logout().permitAll()
        ;

        httpSecurity
                .logout(l -> l
                        .logoutSuccessUrl("/success-logout").permitAll()
                );
    }

}
