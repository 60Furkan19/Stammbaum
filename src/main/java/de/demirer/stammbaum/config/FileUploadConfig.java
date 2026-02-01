package de.demirer.stammbaum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    // Multipart-Konfiguration wird über application.properties definiert
    // spring.servlet.multipart.max-file-size=5MB
    // spring.servlet.multipart.max-request-size=5MB
}

