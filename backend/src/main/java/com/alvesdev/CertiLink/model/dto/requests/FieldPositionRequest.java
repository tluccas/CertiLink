package com.alvesdev.CertiLink.model.dto.requests;

public record FieldPositionRequest(
    String fieldName,
    Double xCoordinate,
    Double yCoordinate,
    Integer fontSize,
    String fontColor
) {}
