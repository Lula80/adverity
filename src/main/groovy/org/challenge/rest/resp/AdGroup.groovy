package org.challenge.rest.resp;

import com.fasterxml.jackson.annotation.*;

/**
 * Represents groups , obtained from grouping list of results by certain criteria
 * (set of dimensions from request)
 */
class  AdGroup{
     private Map<String, Object> groups = new HashMap<>();

     @JsonAnyGetter
     Map<String, Object> getGroups() {
         return groups;
     }

     @JsonAnySetter
     void addGroup(String key, Object value) {
        if(!this.groups)
        {
            this.groups = new HashMap<>(1);
        }
        this.groups.put(key,value);
    }
}
