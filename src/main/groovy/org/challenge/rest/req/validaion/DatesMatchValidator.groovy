package org.challenge.rest.req.validaion

import org.apache.commons.beanutils.BeanUtils
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import java.time.LocalDate

class DatesMatchValidator implements ConstraintValidator<DatesMatch, Object>
{
  private String from
  private String to

  @Override
   void initialize(final DatesMatch constraintAnnotation)
  {
    from = constraintAnnotation.from()
    to = constraintAnnotation.to()
  }

  @Override
   boolean isValid(final Object value, final ConstraintValidatorContext context)
  {
    try
    {
      final LocalDate fromDate = LocalDate.parse(BeanUtils.getProperty(value, from))
      final LocalDate toDate = LocalDate.parse(BeanUtils.getProperty(value, to))

      boolean matches = (fromDate && toDate && fromDate.isBefore(toDate))
      return matches
    }
    catch (final Exception ex)
    {

    }
    return true
  }

}
