package cn.worth.file.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-29
 * @description 回调传递参数
 **/
@Data
public class CallBack implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * oss文件的路径存储地址
     */
    private String filePath;

    /**
     * 文件地址前缀
     */
    private String prePath;

    /**
     * 归属应用
     */
    private String belongName;

    /**
     * 归属应用类别
     */
    private String belongType;

    /**
     * 文件的大小
     */
    private long size;
    private String mimeType;

    private String width;
    private String height;
}