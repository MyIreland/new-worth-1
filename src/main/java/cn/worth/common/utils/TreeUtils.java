package cn.worth.common.utils;

import cn.worth.common.domain.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chenxiaoqing9
 * @Date: Created in 2019/3/19
 * @Description: 工具类
 * @Modified by:
 */
public class TreeUtils {
    /**
     * 返回树状结构数据
     *
     * @param treeNodes
     * @param pid
     * @param <T>
     * @return
     */
    public static <T extends TreeNode> List<T> buildTree(List<T> treeNodes, Object pid) {
        List<T> vos = new ArrayList<>();
        for (T node : treeNodes) {
            Long parentId = node.getParentId();
            Long id = node.getId();
            if (parentId.longValue() == ((Long) pid).longValue()) {
                List<T> ts = buildTree(treeNodes, id);
                node.setChildren(ts);
                vos.add(node);
            }
        }
        return vos;
    }

}
