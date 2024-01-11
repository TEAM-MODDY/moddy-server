package com.moddy.server.controller.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moddy.server.controller.model.dto.request.ModelHairServiceRequest;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;

import java.util.ArrayList;
import java.util.List;

public class ListPropertyEditor extends CustomCollectionEditor {

    private final Class<?> elementType;

    public ListPropertyEditor(Class<?> elementType) {
        super(ArrayList.class);
        this.elementType = elementType;
    }

    @Override
    protected Object convertElement(Object element) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue((String) element,  new TypeReference<List<ModelHairServiceRequest>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert element", e);
        }
    }

    @Override
    public void setAsText(String text) {
        // 전체 문자열을 변환하는 메서드 오버라이드
        setValue(text);
    }
}

////
//public class ListPropertyEditor extends CustomCollectionEditor {
//
//    private final Class<?> elementType;
//
//    public ListPropertyEditor(Class<?> elementType) {
//        super(ArrayList.class);
//        this.elementType = elementType;
//    }
//
//    @Override
//    protected Object convertElement(Object element) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readValue((String) element, new TypeReference<List<Object>>() {});
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Failed to convert element", e);
//        }
//    }
//}