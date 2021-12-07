package org.challenge.rest.req.validaion;

import org.apache.commons.beanutils.BeanUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DatesMatchValidator implements ConstraintValidator<DatesMatch, Object>
{
  private String from;
  private String to;

  @Override
  public void initialize(final DatesMatch constraintAnnotation)
  {
    from = constraintAnnotation.from();
    to = constraintAnnotation.to();
  }

  @Override
  public boolean isValid(final Object value, final ConstraintValidatorContext context)
  {
    try
    {
      final LocalDate fromDate = LocalDate.parse(BeanUtils.getProperty(value, from));
      final LocalDate toDate = LocalDate.parse(BeanUtils.getProperty(value, to));

      boolean matches = (null != fromDate && null != toDate && fromDate.isBefore(toDate));
      return matches;
    }
    catch (final Exception ex)
    {

    }
    return true;
  }

}
