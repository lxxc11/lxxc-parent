package com.lvxc.zwdd.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZwddResponse implements Serializable {

    private String data ;

    private String responseMessage ;

    private String responseCode ;

    private boolean success ;
}
