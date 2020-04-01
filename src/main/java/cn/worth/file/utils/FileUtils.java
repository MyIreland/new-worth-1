package cn.worth.file.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import cn.worth.common.utils.DateUtils;
import cn.worth.common.utils.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-11
 * @description
 **/
public class FileUtils {

    /**
     *  输出到客户端
     * @param response
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void write(HttpServletResponse response, String fileName, byte[] data) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+ URLUtil.encode(fileName));
        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }


    public static void uploadFile(byte[] file, String uploadPath, String fileDir, String fileName) throws Exception {
        File targetFile = new File(uploadPath + fileDir);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(uploadPath + fileDir + "/" + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static void downloadFile(HttpServletResponse response, String originDir, String fileUrl) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        FileInputStream inputStream = new FileInputStream(originDir + "/" + fileUrl);
        byte[] bytes = IOUtils.toByteArray(inputStream);

        response.addHeader("Content-Length", bytes.length + "");//解决大文件导出时出问题

        IOUtils.write(bytes, out);
        IOUtils.closeQuietly(out);
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    public static String generatorFileDir(String module, String bizType) {
        StringBuilder sb = new StringBuilder();
        sb.append("/files/");
        if (StringUtils.isNotBlank(module)) {
            sb.append(module).append("/");
        }
        if (StringUtils.isNotBlank(bizType)) {
            sb.append(bizType).append("/");
        }
        String dateDir = DateUtils.formatDate(new Date());//日期文件夹
        sb.append(dateDir);
        return sb.toString();
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void resetFileResponseHeader(HttpServletResponse response, String name) {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + name);
        response.setContentType("application/octet-stream; charset=UTF-8");
    }
}
