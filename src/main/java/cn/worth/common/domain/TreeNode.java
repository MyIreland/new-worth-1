package cn.worth.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: MyIreland on.
 * @Description:
 * @Date: 2019/3/24
 */
@Getter
@Setter
public class TreeNode<T extends TreeNode> {
    protected Long id;
    protected Long parentId;
    protected List<T> children = new ArrayList<>();

    public void add(T node) {
        children.add(node);
    }
}
