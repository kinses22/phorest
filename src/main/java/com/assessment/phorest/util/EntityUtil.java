package com.assessment.phorest.util;

import java.beans.PropertyDescriptor;
import java.util.UUID;

public class EntityUtil {

    public static <T> UUID getId(T entity) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor("id", entity.getClass());
            return (UUID) pd.getReadMethod().invoke(entity);
        } catch (Exception e) {
            // todo: more specific exceptions
            throw new RuntimeException("Error getting ID from entity", e);
        }
    }

}
