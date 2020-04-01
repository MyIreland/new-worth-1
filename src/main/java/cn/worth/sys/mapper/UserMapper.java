package cn.worth.sys.mapper;

import cn.worth.sys.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
