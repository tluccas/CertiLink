package com.alvesdev.CertiLink.model.dto.responses;

import java.util.List;

public record FieldPositionsResponse(
    Long templateId,
    List<FieldPositionItem> fields
) {
    public static record FieldPositionItem(
        String fieldName,
        Double xCoordinate,
        Double yCoordinate,
        Integer fontSize,
        String fontColor
    ) {}
}
