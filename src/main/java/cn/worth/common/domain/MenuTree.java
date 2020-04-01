package cn.worth.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: MyIreland on.
 * @Description:
 * @Date: 2019/3/24
 */
@Getter
@Setter
public class MenuTree extends TreeNode<MenuTree> {
    /**
     * 菜单名称
     */
    private String label;
    /**
     * 排序值
     */
    private Integer sort;
}
