package com.zzq.system.core;

import cn.hutool.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.util.ResourceUtils;

/**
 * @author Sherlock
 * @copyright freeman
 * @since 2020/7/16 22:20
 */
public class MakeCsvFile {

    public static String makeWithDbData(List list) throws IOException {
        final String uuid = UUID.randomUUID().toString();
        File file = new File(System.getProperty("user.temp") + File.pathSeparator + uuid + ".csv");
        try (FileOutputStream out = new FileOutputStream(file)) {
            for (Object o : list) {
                JSONObject jsonObject = new JSONObject(o);
                out.write(jsonObject.toJSONString(0).getBytes());
            }
            out.flush();
        }
        return file.getAbsolutePath();
    }

    public static File getTestFilePath() throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:data.txt");
    }
}
