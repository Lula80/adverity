package org.challenge.rest.req.validaion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target({TYPE, ANNOTATION_TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DatesMatchValidator.class)
@Documented
public @interface DatesMatch {
  String message() default "{constraints.date.match}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * @return The first date
   */
  String from();

  /**
   * @return The second date
   */
  String to();

  /**
   * Defines several <code>@DatesMatch</code> annotations on the same element
   *
   * @see DatesMatch
   */
  @Target({TYPE, ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List
  {
    DatesMatch[] value();
  }
}
