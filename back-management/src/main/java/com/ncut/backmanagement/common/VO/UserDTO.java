package com.ncut.backmanagement.common.VO;

import lombok.Data;

/**
 * Created by xiadong.
 */
@Data
public class UserDTO {
    private Long id;
    private String name;
    private String avatar;
    private String phoneNumber;
    private String lastLoginTime;

}
