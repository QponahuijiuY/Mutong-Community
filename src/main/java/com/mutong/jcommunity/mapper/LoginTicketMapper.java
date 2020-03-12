package com.mutong.jcommunity.mapper;

import com.mutong.jcommunity.model.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-01 15:39
 * @time_complexity: O()
 */
@Mapper
@Repository
@Deprecated
public interface LoginTicketMapper {

    int insertLoginTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(@Param("ticket") String ticket);

    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
