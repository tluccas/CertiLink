package com.alvesdev.CertiLink.model.dto.responses;

import java.time.LocalDateTime;

public record UploadResponse (
	Long id,
	String slug,
	String name,
	String fileType,
	String filePath,
	Integer pageWidth,
	Integer pageHeight,
	LocalDateTime createdAt
){}
