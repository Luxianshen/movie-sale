package com.github.lujs.model.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/4 18:10
 */
@Data
public class Seat implements Serializable {

    private String columnNo;
    private String rowNo;
    private Integer lovestatus;
    private String areaId;
    private String seatId;
    private String seatNo;
    private String status;
}
