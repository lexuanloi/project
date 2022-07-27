package com.example.demo2.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;

public class FileUploadUtil {
	//lưu file ảnh user
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)	throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Không thể lưu file: " +fileName,ex);
		}
	}
	
	public static void clearDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file ->{
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					}catch (IOException ex) {
						System.out.println("Không thể xoá file:  " + file);
					}
				}
			});
		} catch (IOException ex) {
			System.out.println("Không tìm thấy thư mục: " + dirPath);
		}
	}
	
	public static void removeDir(String dir) {
		clearDir(dir);
		
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			System.out.println("Không tìm thấy thư mục: " + dir);
		}
	}
}