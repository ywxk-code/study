package com.woniuxy.vo;

import lombok.Data;

import java.util.List;
@Data
public class SetPermissionVO {
    private Integer rid;
    private List<Integer> pids;
}
