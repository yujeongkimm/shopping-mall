package com.shop.config;


import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecutiryConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    //http 요청에 대한 보안 설정 (페이지 권한, 로그인 페이지, 로그아웃 메서드 설정 작성)
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .mvcMatchers("/", "/members/**",
                        "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    //BCryptPasswordEncoder() 해쉬함수 이용해서 비밀번호를 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //spring Security에서 인증은 AuthenticationManagerBuilder 통해 이뤄짐
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService)  //userDetail을 구현하는 객체로 memberService 지정
                .passwordEncoder(passwordEncoder());    //비밀번호 암호화
    }
}
