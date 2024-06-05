package com.bronya.qqchat.util;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/6/4
 * @since 1.0.0
 */
@Component
public class ImagesUtils {


    public static void save(String path, String fileName, MultipartFile image) throws IOException {
        File file = new File(path, fileName+".jpg");
        image.transferTo(file);
    }

}