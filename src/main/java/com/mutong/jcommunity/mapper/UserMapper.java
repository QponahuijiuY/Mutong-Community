package com.mutong.jcommunity.mapper;

import com.mutong.jcommunity.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-18 14:25
 * @time_complexity: O()
 */
@Mapper
@Repository
public interface UserMapper {
    User selectById( @Param("id") int id);

    User selectByName(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    int insertUser(User user);

    int updateStatus(@Param("id") int id, @Param("status") int status);

    int updateHeader(@Param("id") int id, @Param("headerUrl") String headerUrl);

    int updatePassword(@Param("id")int id, @Param("password") String password);
}
