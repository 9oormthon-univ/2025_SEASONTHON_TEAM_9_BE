package com.clucid.server.infra.s3;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileNameConstructor {
	public static String constructTermImageFileName(String userId, MultipartFile image) {
		return "term/" + makeImageName(userId, image);
	}

	private static String makeImageName(String userId, MultipartFile image) {

		String contentType = image.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("이미지 파일이 아닙니다.");
		}

		String suffix = "";
		if ("image/png".equals(contentType))
			suffix = ".png";
		else if ("image/jpeg".equals(contentType))
			suffix = ".jpg";
		else if ("image/gif".equals(contentType))
			suffix = ".gif";

		String subUserId = userId.substring(0, 8);
		String randomId = UUID.randomUUID().toString().substring(0, 8);

		return image.getName() + "_" + subUserId + "_" + randomId + suffix;
	}
}
