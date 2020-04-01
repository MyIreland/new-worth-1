package cn.worth.core.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: MyIreland on.
 * @Description:
 * @Date: 2019/3/22
 */
public abstract class BaseController<E extends IService<T>, T> {

    @Autowired
    protected E baseService;
}