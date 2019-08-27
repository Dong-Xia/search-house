package com.ncut.backmanagement.common;

import lombok.Data;

import java.util.List;

/**
 * 通用多结果Service返回结构
 * Created by xiadong.
 */
@Data
public class ServiceMultiResult<T> {
    private Integer total;
    private List<T> result;

    public ServiceMultiResult(Integer total, List<T> result) {
        this.total = total;
        this.result = result;
    }
}
