package com.moddy.server.controller.model.dto.requestEditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.List;


public class ListPropertyEditor extends PropertyEditorSupport {

    private final Class<?> elementType;
    private final ObjectMapper objectMapper;

    public ListPropertyEditor(Class<?> elementType) {
        this.elementType = elementType;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                List<?> list = objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
                setValue(list);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to convert value to List: " + text, e);
            }
        } else {
            setValue(null);
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to convert value to JSON: " + value, e);
        }
    }
}
