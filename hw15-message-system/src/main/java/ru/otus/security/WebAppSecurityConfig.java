package ru.otus.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // базовая авторизация для тестирования, так как без нее пришлось бы делать авторизацию для юзера 1 из БД, а это уже выходит за рамки ДЗ. Я пробовал разобраться, как сделать авторизацию через БД в Spring Boot, но пока не очень понял, как можно совместить ее с моим классом UserLoginService
        UserBuilder users = User.withDefaultPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser(users.username("admin").password("111111").roles("ADMIN"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Чтобы корректно принимал русские буквы помогло только это !
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .defaultSuccessUrl("/admin")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();

        http.formLogin().defaultSuccessUrl("/admin", true);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/");
    }

}






