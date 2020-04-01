package cn.worth.sys.controller;

import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.common.utils.CollectionUtils;
import cn.worth.common.utils.StringUtils;
import cn.worth.core.controller.BaseController;
import cn.worth.sys.domain.Dict;
import cn.worth.sys.param.BatchDelDictParam;
import cn.worth.sys.service.IDictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-08-07
 */
@RestController
@RequestMapping("dict")
public class DictController extends BaseController<IDictService, Dict> {

    @Autowired
    private IDictService dictService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return Dict
     */
    @GetMapping("{id}")
    public R<Dict> get(@PathVariable Long id) {
        return R.success(dictService.getById(id));
    }


    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @PostMapping("/page")
    public R page(Page<Dict> entityPage, Dict query) {

        QueryWrapper conditionWrapper = getDictQueryWrapper(query);

        Page<Dict> page = dictService.page(entityPage, conditionWrapper);
        return R.success(page);
    }

    private QueryWrapper getDictQueryWrapper(Dict query) {
        QueryWrapper<Dict> conditionWrapper = new QueryWrapper<>();
        conditionWrapper.orderByAsc("id", "type", "sort");
        String description = query.getDescription();
        String type = query.getType();
        if(StringUtils.isNotBlank(description)){
            conditionWrapper.eq("description", description);
        }
        if(StringUtils.isNotBlank(type)){
            conditionWrapper.eq("type", type);
        }
        conditionWrapper.eq("del_flag", CommonConstant.STATUS_NORMAL);
        return conditionWrapper;
    }

    /**
     * 添加
     *
     * @param dict 实体
     * @return success/false
     */
    @PostMapping
    public R add(@RequestBody Dict dict) {
        return R.success(dictService.save(dict));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        Dict dict = new Dict();
        dict.setId(id);
        dict.setDelFlag(CommonConstant.STATUS_DEL);
        dict.setGmtUpdate(new Date());
        return R.success(dictService.updateById(dict));
    }

    /**
     * 删除
     *
     * @param param
     * @return success/false
     */
    @PostMapping("batchDel")
    public R batchDel(@RequestBody BatchDelDictParam param) {
        List<Long> ids = param.getIds();
        if(CollectionUtils.isNotEmpty(ids)){
            List<Dict> dicts = new ArrayList<>();
            for (Long id : ids) {
                Dict dict = new Dict();
                dict.setId(id);
                dict.setDelFlag(CommonConstant.STATUS_DEL);
                dicts.add(dict);
            }
            dictService.updateBatchById(dicts);
        }

        return R.success(true);
    }

    /**
     * 编辑
     *
     * @param dict 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody Dict dict) {
        dict.setGmtUpdate(new Date());
        return R.success(dictService.updateById(dict));
    }
}
