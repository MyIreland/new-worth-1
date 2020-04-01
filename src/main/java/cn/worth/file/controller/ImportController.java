package cn.worth.file.controller;

import cn.worth.common.domain.R;
import cn.worth.core.controller.BaseController;
import cn.worth.file.domain.Import;
import cn.worth.file.service.IFileService;
import cn.worth.file.service.IImportService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-10-11
 */
@RestController
@RequestMapping("import")
public class ImportController extends BaseController<IImportService, Import> {

    @Autowired
    private IFileService fileService;

    @PostMapping("files")
    public R importFile(MultipartFile[] files) {
        return null;
    }

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return Import
     */
    @GetMapping("/{id}")
    public R<Import> get(@PathVariable Long id) {
        return new R<>(baseService.getById(id));
    }


    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @PostMapping("/page")
    public R page(Page<Import> entityPage, Import entity) {
        Page<Import> page = baseService.page(entityPage, null);
        return R.success(page);
    }

    /**
     * 添加
     *
     * @return success/false
     */
    @PostMapping("add")
    public R<Boolean> add(@RequestBody Import importDO) {
        return new R<>(baseService.save(importDO));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        Import importDO = new Import();
        importDO.setId(id);
        return new R<>(baseService.updateById(importDO));
    }

    /**
     * 编辑
     *
     * @return success/false
     */
    @PutMapping("edit")
    public R<Boolean> edit(@RequestBody Import importDO) {
        return new R<>(baseService.updateById(importDO));
    }
}
