package com.example.project_sem_4.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinaryConfig;

    public String uploadFile(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap("folder","project_ky_4"));
            return  uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse findFile(String asset_id) {
        try {
            return cloudinaryConfig.search().expression("resource_type:image AND folder=project_ky_4 AND asset_id="+ asset_id).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse findAll(int limit,String next_cursor) {
        //Paginate của clouddinary chỉ có thể next
        try {
            if (next_cursor != null && next_cursor.length() > 0){
                return cloudinaryConfig
                        .search()
                        .expression("resource_type:image AND folder=project_ky_4")
                        .maxResults(limit)
                        .nextCursor(next_cursor)
                        .execute();
            }

            return cloudinaryConfig
                    .search()
                    .expression("resource_type:image AND folder=project_ky_4")
                    .maxResults(limit)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map deleteFile(String public_id) {
        try {
            return cloudinaryConfig.uploader().destroy(public_id, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
