package net.datasa.web5.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileService {

    public String saveFile(MultipartFile mfile, String uploadPath) {
        if (mfile == null || mfile.isEmpty() || mfile.getSize() == 0) {
            return null;
        }

        File path = new File(uploadPath);
        if (!path.isDirectory()) {
            path.mkdirs();
        }

        String originalFilename = mfile.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String savedFilename = sdf.format(new Date()) + "_" + originalFilename;

        try {
            mfile.transferTo(new File(uploadPath, savedFilename));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return savedFilename;
    }

    public boolean deleteFile(String fullPath) {
        File file = new File(fullPath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
