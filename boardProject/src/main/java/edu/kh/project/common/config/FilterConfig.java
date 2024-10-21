package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;
import edu.kh.project.common.filter.SignUpFilter;
import oracle.sql.ARRAY;

/* 사용할 필터 클래스를 Bean으로 등록 + 적용될 요청 주소 설정 */

@Configuration
public class FilterConfig {
	
	@Bean 
	public FilterRegistrationBean<SignUpFilter> signUpFilter(){
		
		// FilterRegistrationBean : 필터를 Bean으로 등록하는 객체
		FilterRegistrationBean<SignUpFilter> filter
			= new FilterRegistrationBean<>();
		
		// 등록할 코드가 doFilter() 메서드에 작성된
		// 필터 객체(SignUpFilter) 생성
		SignUpFilter signUpFilter = new SignUpFilter();
		
		filter.setFilter(signUpFilter); // 필터 등록
		
		// 필터가 동작할 요청 경로 패턴 지정
		String[] filteringUrl = {"/member/signUp"};
		filter.setUrlPatterns( Arrays.asList(filteringUrl));
		
		// 필터 이름 지정
		filter.setName("signUpFilter");
		
		// 필터 순서 지정
		filter.setOrder(1);
		
		
		return filter;
	}
	
	@Bean
	public FilterRegistrationBean<LoginFilter> loginfilter(){
		
		FilterRegistrationBean<LoginFilter> filter
		 = new FilterRegistrationBean<>();
		
		LoginFilter loginFilter = new LoginFilter();
		
		filter.setFilter(loginFilter);
		
		String[] filteringUrl = {"/myPage/*", "/editBoard/*"};
		filter.setUrlPatterns( Arrays.asList(filteringUrl) );
		
		filter.setName("loginFilter");
		
		filter.setOrder(2);
		
		
		return filter;
	}
}
