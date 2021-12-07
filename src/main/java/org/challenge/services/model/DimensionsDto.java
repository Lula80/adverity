package org.challenge.services.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.querydsl.core.Tuple;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.challenge.rest.req.DateRange;
import org.challenge.services.model.Dimension;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionsDto{// extends BeanUtilsBean {


//    @Override
//    public void copyProperty(Object dest, String name, Object value)
//            throws IllegalAccessException, InvocationTargetException {
//        if(value==null)return;
//        super.copyProperty(dest, name, value);
//    }
    Metric metric;
    List<Dimension> dimensions;
    String aggregate;

}
