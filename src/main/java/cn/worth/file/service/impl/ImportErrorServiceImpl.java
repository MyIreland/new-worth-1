package cn.worth.file.service.impl;

import cn.worth.file.domain.ImportError;
import cn.worth.file.mapper.ImportErrorMapper;
import cn.worth.file.service.IImportErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-10-11
 */
@Service
public class ImportErrorServiceImpl extends ServiceImpl<ImportErrorMapper, ImportError> implements IImportErrorService {

}
