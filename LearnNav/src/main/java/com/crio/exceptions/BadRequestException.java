package com.crio.exceptions;

public class BadRequestException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public BadRequestException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Bad request for %s with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}