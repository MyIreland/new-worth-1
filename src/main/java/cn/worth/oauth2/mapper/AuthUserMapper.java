package cn.worth.oauth2.mapper;

import cn.worth.core.domain.LoginUser;
import cn.worth.oauth2.domain.AuthUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-01
 * @description
 **/
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {
    LoginUser loadUserByUsername(AuthUser userQuery);
}

