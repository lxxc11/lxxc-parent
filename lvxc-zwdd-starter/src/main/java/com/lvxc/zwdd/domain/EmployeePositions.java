package com.lvxc.zwdd.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePositions implements Serializable {

    private String govEmpPosJob ;

    private String employeeCode ;
    //是否主职
    private Boolean mainJob ;
}
