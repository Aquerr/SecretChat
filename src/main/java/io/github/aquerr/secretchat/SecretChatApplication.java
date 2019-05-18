package io.github.aquerr.secretchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SecretChatApplication extends SpringBootServletInitializer
{

	public static void main(String[] args) {
		SpringApplication.run(SecretChatApplication.class, args);
//		SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
//		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//		internalResourceViewResolver.set
//		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
//		urlBasedViewResolver.setViewNames();
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
	{
		return builder.sources(SecretChatApplication.class);
	}
}
