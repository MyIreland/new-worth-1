package cn.worth.file.controller;

import cn.worth.common.domain.R;
import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.DateUtils;
import cn.worth.file.config.FileProps;
import cn.worth.file.domain.File;
import cn.worth.file.service.IFileService;
import cn.worth.file.utils.*;
import cn.worth.file.vo.CallBack;
import cn.worth.file.vo.FileDown;
import cn.worth.file.vo.FilePath;
import cn.worth.file.vo.SignDTO;
import com.google.common.collect.Lists;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-29
 * @description
 **/
@RestController
@RequestMapping("/oss")
@Slf4j
public class OssController {

    @Autowired
    private FileProps fileProps;

    @Autowired
    private IFileService fileService;


    /**
     * 签名生成  用浏览器直接上传时给js传递授权
     *
     * @param callback
     * @param type     必填。防止恶意修改路径
     * @return
     */
    @CrossOrigin
    @GetMapping("/policy")
    public R policy(@RequestParam(required = false) String callback, String type) {

        FilePath filePath = OssUploadUtils.getFilePath(fileProps.getFilePaths(), type);
        if (filePath == null) {
            return R.fail("没有对应的文件类型");
        }
        if (StringUtils.isNotBlank(callback)) {
            filePath.setCallback(callback);
        }
        SignDTO policy = OssUploadUtils.policy(filePath);
        return R.success(policy);
    }


    /**
     * 上传成功回调方法
     *
     * @param callBack
     * @return
     */
    @RequestMapping("callback")
    public R callback(CallBack callBack) {

        String filename = FileUtil.getName(callBack.getFilePath());
        String ossFileUrl = OssUploadUtils.getOssFileUrl(callBack.getFilePath());
        String fileSuffix = FileUtil.getFileSuffix(filename);
        File file = new File();
        file.setUrl(ossFileUrl);
        file.setName(callBack.getBelongName());
        file.setType(FileTypeUtils.fileType(fileSuffix));
        file.setSize(callBack.getSize());
        file.setBizType(callBack.getBelongType());
        file.setGmtCreate(new Date());
        fileService.save(file);
        return R.success(callBack);
    }


    /**
     * 根据单个或多个文件进行 zip下载
     * 文件路径和id都有 优先处理url的 默认名称yyyyMMddHHmmss.zip;
     * 可以根据文件id 或者路径下载；建议使用路径下载
     *
     * @param fileDown
     */
    @GetMapping("downFilesZip")
    public void downFilesZip(FileDown fileDown, HttpServletResponse response) {

        // 处理文件路径
        List<String> urls = Lists.newArrayList();
        if (StringUtils.isBlank(fileDown.getUrls())) {
            String[] ids = fileDown.getIds().split(",");
            for (String id : ids) {
                File byId = fileService.getById(id);
                urls.add(byId.getUrl());
            }
        } else {
            urls = StrUtil.splitTrim(fileDown.getUrls(), ",");
        }
        // 处理默认fileName
        String fileName = fileDown.getFileName();
        if (StrUtil.isBlank(fileName)) {
            fileName = DateUtils.getCurrentShortDateTimeStr().concat(".zip");
        }
        byte[] bytes = OssDownUtils.writeZip(urls);
        try {
            FileUtils.write(response, fileName, bytes);
        } catch (IOException e) {
            log.info("出现异常！");
            throw new BusinessException("出现异常！");
        }

    }


    /**
     * 根据文件 id 或者路径下载 文件
     * 文件路径和id都有 优先处理url的 默认名称为原文件名称
     *
     * @param fileDown
     * @param response
     * @throws IOException
     */
    @GetMapping("downFile")
    public void downFiles(FileDown fileDown, HttpServletResponse response) throws IOException {

        String url = fileDown.getUrls();

        if (StrUtil.isBlank(url)) {
            File byId = fileService.getById(fileDown.getIds());
            url = byId.getUrl();
        }
        String fileName = fileDown.getFileName();
        if (StrUtil.isBlank(fileName)) {
            fileName = FileUtil.getName(url);
        }
        OssDownUtils.write(response, fileName, url);
    }

}
