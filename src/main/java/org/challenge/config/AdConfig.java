package org.challenge.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.challenge.utils.Constants;
import org.challenge.utils.ContextualMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Locale;

@Configuration
public class AdConfig{

  @Bean
  public Validator getDefaultValidator() {
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    Validator validator = Validation.buildDefaultValidatorFactory().usingContext()
            .messageInterpolator((MessageInterpolator) new ContextualMessageInterpolator(new ResourceBundleMessageInterpolator(
                    new PlatformResourceBundleLocator(Constants.ERROR_MESSAGES))))
            .getValidator();
    return validator;
  }

  @Bean
  public LocaleResolver localeResolver() {
    FixedLocaleResolver localeResolver = new FixedLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    return localeResolver;
  }

}
