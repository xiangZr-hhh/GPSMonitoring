package com.gps.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (GpsData)表实体类
 *
 * @author zrx
 * @since 2024-05-20 19:24:36
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("gps_data")
public class GpsData {
    @TableId(type= IdType.AUTO)
    private Long id;
    //所属gpsId
    private Long gpsId;
    //经度
    private Double jingdu;
    //维度
    private Double weidu;
    //创建时间
    private Date createdTime;


}
