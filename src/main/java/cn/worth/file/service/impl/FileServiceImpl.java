package cn.worth.file.service.impl;

import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.AssertUtils;
import cn.worth.common.utils.CollectionUtils;
import cn.worth.file.domain.File;
import cn.worth.file.enums.FileErrorEnum;
import cn.worth.file.mapper.FileMapper;
import cn.worth.file.service.IFileService;
import cn.worth.file.utils.FileTypeUtils;
import cn.worth.file.utils.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文件上传 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-11
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Value("${worth.uploadPath}")
    private String uploadPath;

    @Override
    public List<File> batchUpload(MultipartFile[] files, String module, String bizType) {
        if (null == files || files.length == 0) {
            throw new BusinessException("上传文件不能为空");
        }
        List<File> fileList = new ArrayList<>();
        for (MultipartFile each : files) {
            String originalFilename = each.getOriginalFilename();

            String fileDir = FileUtils.generatorFileDir(module, bizType);
            String fileName = FileUtils.renameToUUID(originalFilename);
            File file = generatorFile(fileDir, fileName, bizType);
            file.setName(originalFilename);
            file.setSize(each.getSize());
            file.setType(FileTypeUtils.fileType(originalFilename));
            try {
                FileUtils.uploadFile(each.getBytes(), uploadPath, fileDir, fileName);
                fileList.add(file);
            } catch (Exception e) {
                log.error("upload file:{} fail", originalFilename);
            }
        }
        //存储文件记录
        saveBatch(fileList);
        return fileList;
    }

    @Override
    public void download(HttpServletResponse response, String fileUrl) {
        AssertUtils.isNull(fileUrl, "文件路径不正确");
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("url", fileUrl);

        File file = getOne(wrapper);
        if (null == file) {
            FileErrorEnum unknowFile = FileErrorEnum.UNKNOW_FILE;
            throw new BusinessException(unknowFile.getCode(), unknowFile.getDesc());
        }
        try {
            FileUtils.resetFileResponseHeader(response, file.getName());

            FileUtils.downloadFile(response, uploadPath, fileUrl);
        } catch (IOException e) {
            log.error("download file fail:{}", e.getMessage());
            throw new BusinessException("抱歉，未找到指定资源文件！");
        } catch (Exception e) {
            log.error("download file fail:{}", e.getMessage());
            throw new BusinessException("未知异常，请联系管理员！");
        }
    }

    @Override
    public Boolean add(File file) {
        AssertUtils.isNull(file.getBizType(), "文件业务类型不能为空");
        AssertUtils.isNull(file.getUrl(), "文件路径不能为空");
        String fileName = file.getName();
        AssertUtils.isNull(fileName, "文件名不能为空");
        file.setGmtCreate(new Date());
        file.setType(FileTypeUtils.fileType(fileName));
        return save(file);
    }

    @Override
    public Boolean del(Long id) {
        AssertUtils.isNull(id, "id is null");
        File file = getById(id);
        if (null == file) {
            throw new BusinessException("文件不存在");
        }

        String fileUrl = uploadPath + "/" + file.getUrl();
        if (removeById(id)) {
            if (!FileUtils.deleteFile(fileUrl)) {
                log.info("del file data success, del file exception:{}", file.getUrl());
            }
            return true;
        } else {
            log.info("del file data fail:{}", file.getId());
            return false;
        }
    }

    @Override
    public Boolean delBatch(List<Long> ids) {
        if(CollectionUtils.isNotEmpty(ids)){
            for (Long id : ids) {
                del(id);
            }
        }
        return true;
    }

    private File generatorFile(String fileDir, String fileName, String bizType){
        File file = new File();
        file.setUrl(fileDir + "/" + fileName);
        file.setBizType(bizType);
        file.setGmtCreate(new Date());
        return file;
    }
}
