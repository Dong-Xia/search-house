package com.ncut.backmanagement.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ncut.backmanagement.dao.RoleMapper;
import com.ncut.backmanagement.domain.Role;
import com.ncut.backmanagement.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author xiadong
 * @since 2019-08-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
