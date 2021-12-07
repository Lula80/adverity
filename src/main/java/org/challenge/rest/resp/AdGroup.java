package org.challenge.rest.resp;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents groups , obtained from grouping list of results by certain criteria
 * (set of dimensions from request)
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  AdGroup{

   Map<String, Object> groups = new HashMap<>();
    public AdGroup(){
    }

     @JsonAnyGetter
     public Map<String, Object> getGroups() {
         return groups;
     }

     @JsonAnySetter
     public void addGroup(String key, Object value) {
        if(this.groups == null)
        {
            this.groups = new HashMap<>(1);
        }
        this.groups.put(key,value);
    }
}
