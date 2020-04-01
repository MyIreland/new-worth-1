package cn.worth.file.utils;

import cn.worth.file.domain.File;
import com.google.common.collect.Maps;

import java.util.Map;

public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 视频类型对应的值
     */
    public static final Integer VIDEO_TYPE = 0;

    /**
     * 音频类型对应的值
     */
    public static final Integer AUDIO_TYPE = 1;

    /**
     * zip类型对应的值
     */
    public static final Integer ZIP_TYPE = 2;

    /**
     * doc类型对应的值
     */
    public static final Integer DOC_TYPE = 3;

    /**
     * 图片类型对应的值
     */
    public static final Integer IMAGE_TYPE = 4;

    /**
     * 其他类型对应的值
     */
    public static final Integer OTHER_TYPE = 9;

    /**
     * 视频类型
     */
    public static Map<String, String> videoTypeMap = Maps.newHashMap();
    static {
        videoTypeMap.put("mp4", "mp4");
        videoTypeMap.put("flv", "flv");
        videoTypeMap.put("rm", "rm");
        videoTypeMap.put("rmvb", "rmvb");
        videoTypeMap.put("wmv", "wmv");
    }

    /**
     * 音频类型
     */
    public static Map<String, String> audioTypeMap = Maps.newHashMap();
    static {
        audioTypeMap.put("mp3", "mp3");
        audioTypeMap.put("wav", "wav");
        audioTypeMap.put("avi", "avi");
    }

    /**
     * 压缩包类型
     */
    public static Map<String, String> zipTypeMap = Maps.newHashMap();
    static {
        zipTypeMap.put("zip", "zip");
        zipTypeMap.put("rar", "rar");
        zipTypeMap.put("tar", "tar");
    }

    /**
     * 文档类型
     */
    public static Map<String, String> docTypeMap = Maps.newHashMap();
    static {
        docTypeMap.put("doc", "doc");
        docTypeMap.put("docx", "docx");
        docTypeMap.put("pdf", "pdf");
        docTypeMap.put("txt", "txt");
        docTypeMap.put("csv", "csv");
        docTypeMap.put("pptx", "pptx");
        docTypeMap.put("xlsx", "xlsx");
        docTypeMap.put("xls", "xls");
    }

    /**
     * 图片类型
     */
    public static Map<String, String> imageTypeMap = Maps.newHashMap();
    static {
        imageTypeMap.put("jpg", "jpg");
        imageTypeMap.put("png", "png");
        imageTypeMap.put("gif", "gif");
    }


    /**
     * 根据文件类型获取对应文件的类型
     *     优先把容易匹配的放在判断前面
     * @param fileSuffix 文件后缀 不带.
     * @return 文件类型值
     */
    public static Integer getFileType(String fileSuffix) {
        if (FileUtil.docTypeMap.get(fileSuffix) != null) {
            return DOC_TYPE;
        } else if (FileUtil.imageTypeMap.get(fileSuffix) != null) {
            return IMAGE_TYPE;
        } else if (FileUtil.zipTypeMap.get(fileSuffix) != null) {
            return ZIP_TYPE;
        } else if (FileUtil.audioTypeMap.get(fileSuffix) != null) {
            return AUDIO_TYPE;
        } else if (FileUtil.videoTypeMap.get(fileSuffix) != null) {
            return VIDEO_TYPE;
        }
        return OTHER_TYPE;
    }


    /**
     * 根据文件名称 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 根据文件 获取文件后缀
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}