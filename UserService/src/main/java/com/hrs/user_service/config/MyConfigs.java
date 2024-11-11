package com.hrs.user_service.config;

import com.hrs.user_service.config.interceptor.RestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyConfigs {

    private ClientRegistrationRepository registrationRepository;
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    @Autowired
    public MyConfigs(ClientRegistrationRepository registrationRepository, OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) {
        this.registrationRepository = registrationRepository;
        this.oAuth2AuthorizedClientRepository = oAuth2AuthorizedClientRepository;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {

        RestTemplate template = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptor = new ArrayList<>();
        interceptor.add(new RestTemplateInterceptor(manager(registrationRepository, oAuth2AuthorizedClientRepository)));
        template.setInterceptors(interceptor);
        return template;
    }

    /*declaring the bean of OAuth2AuthorizedClientManager*/
    @Bean
    public OAuth2AuthorizedClientManager manager(
            ClientRegistrationRepository registrationRepository,
            OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository
    ) {
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();

        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(registrationRepository, oAuth2AuthorizedClientRepository);

        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(provider);
        return defaultOAuth2AuthorizedClientManager;
    }
}
