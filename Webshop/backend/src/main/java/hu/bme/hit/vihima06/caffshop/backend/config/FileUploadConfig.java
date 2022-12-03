package hu.bme.hit.vihima06.caffshop.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class FileUploadConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new
                CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10 * 1024 * 1024);
        return multipartResolver;
    }
}
