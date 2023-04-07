package com.github.lujs.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/4 18:10
 */
@AllArgsConstructor
@Data
public class SeatNew implements Serializable {

    private String id;

    /**
     * seatNo 1排1座
     */
    private String seatNo;

    /**
     * rowNo
     */
    private String gRow;

    /**
     * columnNo
     */
    private String gCol;

    /**
     * status N Y
     * 0 普通 0-1 已选 0-2 已售 0-3 维修
     */
    private String type;

    /**
     * lovestatus
     */
    private String flag;

}
