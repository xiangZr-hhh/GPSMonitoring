package com.gps.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gps.model.entity.GpsData;
import com.gps.model.entity.MqttData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (GpsData)表数据库访问层
 *
 * @author zrx
 * @since 2024-05-20 18:53:32
 */
@Mapper
public interface GpsDataMapper extends BaseMapper<GpsData> {

	void insertBatchSomeColumn(@Param("list") List<GpsData> gpsDataList);

}
