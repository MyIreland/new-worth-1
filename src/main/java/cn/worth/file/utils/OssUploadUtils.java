package cn.worth.file.utils;

import cn.worth.common.domain.R;
import cn.worth.common.enums.RCodeEnum;
import cn.worth.common.utils.DateUtils;
import cn.worth.common.utils.StringUtils;
import cn.worth.file.config.OssProps;
import cn.worth.file.vo.CallBack;
import cn.worth.file.vo.FilePath;
import cn.worth.file.vo.SignDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-29
 * @description
 **/
@Slf4j
@Component
public class OssUploadUtils {


    private static OssProps ossProps;

    private static OSS ossInnerClient;

    @Autowired
    public OssUploadUtils(OSS ossInnerClient, OssProps ossProps) {
        OssUploadUtils.ossInnerClient = ossInnerClient;
        OssUploadUtils.ossProps = ossProps;
    }


    /**
     * 根据oss文件存储的路径获取到 外网访问地址
     * 默认过期时间很长
     *
     * @param ossFile oss文件的路径
     * @return
     */
    public static String getOssFileUrl(String ossFile) {

        // 设置URL过期时间为policyExpire  分钟。
        Date expiration = new Date(System.currentTimeMillis() + ossProps.getPolicyExpire() * 60000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossInnerClient.generatePresignedUrl(ossProps.getBucketName(), ossFile, expiration);
        return specialUrlEncode(url.toString());
    }


    /**
     * 根据MultipartFile 文件上传
     *
     * @param file
     * @param filePath
     * @return
     */
    public static R<CallBack> uploadMultipartFile(MultipartFile file, FilePath filePath) {

        InputStream in = null;
        try {
            in = file.getInputStream();
            String urlPath = getFileKey(filePath).concat(file.getOriginalFilename());
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProps.getBucketName(), urlPath, in);
            return getCallBackResult(filePath, putObjectRequest);
        } catch (Exception e) {
            log.info("文件上传 异常");
        } finally {
            IoUtil.close(in);
        }

        return R.fail(RCodeEnum.FAIL.getMsg());
    }

    /**
     * oss 根据 file 上传文件
     *
     * @param file
     * @param filePath
     * @return
     */
    public static R<CallBack> uploadFile(File file, FilePath filePath) {
        FileInputStream inFile = null;
        try {
            inFile = new FileInputStream(file);
            String fileKey = getFileKey(filePath);
            String urlPath = fileKey.concat("/").concat(file.getName());
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProps.getBucketName(), urlPath, inFile);
            return getCallBackResult(filePath, putObjectRequest);
        } catch (Exception e) {
            log.error("文件上传 异常:{}", e.getMessage(), e);
        } finally {
            IoUtil.close(inFile);
        }

        return R.fail(RCodeEnum.FAIL);
    }


    /**
     * 根据文件二进制流上传
     *
     * @param in
     * @param fileName
     * @param filePath
     * @return
     */
    public static R<CallBack> uploadFile(InputStream in, String fileName, FilePath filePath) {
        try {
            String fileKey = getFileKey(filePath);
            String urlPath = fileKey.concat("/").concat(fileName);
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProps.getBucketName(), urlPath, in);
            return getCallBackResult(filePath, putObjectRequest);
        } catch (Exception e) {
            log.error("文件上传 异常");
        } finally {
            IoUtil.close(in);
        }

        return R.fail(RCodeEnum.FAIL.getMsg());
    }

    // -------------------------------------------------------------- 私有的提供上传封装

    /**
     * 设置回调和返回 回调结果
     *
     * @param filePath
     * @param putObjectRequest
     * @return
     * @throws IOException
     */
    private static R<CallBack> getCallBackResult(FilePath filePath, PutObjectRequest putObjectRequest) throws IOException {
        Callback callback = getCallback(filePath);
        putObjectRequest.setCallback(callback);
        PutObjectResult putObjectResult = ossInnerClient.putObject(putObjectRequest);
        byte[] buffer = new byte[1024];
        putObjectResult.getResponse().getContent().read(buffer);
        return JSON.parseObject(new String(buffer), new TypeReference<R<CallBack>>() {
        });
    }

    // -------------------------------------------------------------- 公共的提供上传属性

    /**
     * 获取到上传文件应该存储的路径
     *
     * @param filePath
     * @return
     */
    public static String getFileKey(FilePath filePath) {

        String yyyyMMdd = DateUtils.getCurrentShortDateStr();
        StringBuilder dir = new StringBuilder(filePath.getBelongName());
        dir.append("/").append(filePath.getPrePath())
                .append("/").append(filePath.getBelongType())
                .append("/").append(yyyyMMdd)
                .append("/").append(DateUtils.getCurrentTimeStr());
        return dir.toString();
    }

    /**
     * 获取到回调的callback信息
     *
     * @return
     */
    public static Callback getCallback(FilePath filePath) {
        String callBack = filePath.getCallback();
        if (StringUtils.isBlank(callBack)) {
            callBack = ossProps.getCallback();
        }
        Callback callback = new Callback();
        StringBuilder sbr = new StringBuilder(callBack);
        sbr.append("?belongName=").append(filePath.getBelongName())
                .append("&prePath=").append(filePath.getPrePath())
                .append("&belongType=").append(filePath.getBelongType());
        callback.setCallbackUrl(sbr.toString());
        callback.setCallbackBody("filePath=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCalbackBodyType(Callback.CalbackBodyType.URL);
        return callback;
    }

    /**
     * 根据类型获取到 FilePath
     *
     * @param filePaths FileProps 中的filePaths
     * @param type      FilePath中的类型
     * @return
     */
    public static FilePath getFilePath(List<FilePath> filePaths, String type) {
        FilePath filePath = null;
        for (FilePath item : filePaths) {
            if (item.getType().equals(type)) {
                filePath = item;
                break;
            }
        }
        return filePath;
    }

    /**
     * 特殊符号处理
     *
     * @param value
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static String specialUrlEncode(String value) {
        return value.replace("+", "%2B").replace(" ", "%20");
    }


    /**
     * 签名生成
     *
     * @return
     */
    public static SignDTO policy(FilePath filePath) {

        SignDTO signDTO = new SignDTO();
        // 存储目录

        String dir = OssUploadUtils.getFileKey(filePath);

        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + ossProps.getPolicyExpire() * 600000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        long maxSize = ossProps.getMaxSize() * 1024 * 1024;
        // 回调
        Callback callback = OssUploadUtils.getCallback(filePath);

        // 提交节点
        String action = "http://" + ossProps.getBucketName() + "." + ossProps.getEndpoint();
        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossInnerClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossInnerClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(JSONUtil.toJsonStr(callback).getBytes("utf-8"));
            // 返回结果
            signDTO = SignDTO.builder().accessKeyId(ossProps.getAccessKeyId())
                    .policy(policy).signature(signature).dir(dir)
                    .callback(callbackData).action(action).expire(expiration).build();

        } catch (Exception e) {
            log.error("签名生成失败", e);
        }

        return signDTO;
    }

}

