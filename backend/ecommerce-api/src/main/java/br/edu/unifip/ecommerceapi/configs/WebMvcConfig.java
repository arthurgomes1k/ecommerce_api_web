package br.edu.unifip.ecommerceapi.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CustomPageableSortResolver pageableDefaultSortResolver;

    @Autowired
    public WebMvcConfig(CustomPageableSortResolver pageableDefaultSortResolver) {
        this.pageableDefaultSortResolver = pageableDefaultSortResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(pageableDefaultSortResolver);
    }
}

