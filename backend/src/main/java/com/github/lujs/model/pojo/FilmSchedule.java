package com.github.lujs.model.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/18 18:56
 */
@Data
public class FilmSchedule {

    private String dateStr;

    private List<FilmScheduleDetail> detailList;

}
