package org.challenge.utils;


import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.springframework.context.i18n.LocaleContextHolder;

//https://beanvalidation.org/2.0/spec/#validationapi-message
public class ContextualMessageInterpolator implements MessageInterpolator {

  private final MessageInterpolator delegate;

  public ContextualMessageInterpolator(MessageInterpolator delegate) {
    this.delegate = delegate;
  }

  @Override
  public String interpolate(String template, Context context) {
    return this.delegate.interpolate(template, context, LocaleContextHolder.getLocale());
  }

  @Override
  public String interpolate(String messageTemplate, Context context, Locale locale) {
    return this.delegate.interpolate(messageTemplate, context, locale);
  }

}
