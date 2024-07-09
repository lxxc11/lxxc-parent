package com.lvxc.es.domian;

import lombok.Data;

import java.util.List;

@Data
public class ChartEntity {
    private String name;
    private String value;
    private List<ChartEntity> childChart;
}
