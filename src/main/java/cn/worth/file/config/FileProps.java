package cn.worth.file.config;

import cn.worth.file.vo.FilePath;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class FileProps {

    /**
     * 文件类型
     */
    private List<FilePath> filePaths;
}