package com.ncut.backmanagement.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ncut.backmanagement.dao.UserMapper;
import com.ncut.backmanagement.domain.User;
import com.ncut.backmanagement.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author xiadong
 * @since 2019-08-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
