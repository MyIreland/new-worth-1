package cn.worth.sys.param;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-31
 * @description
 **/
@Getter
@Setter
public class BatchDelDictParam {
    private List<Long> ids;
}
