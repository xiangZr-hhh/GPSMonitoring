package com.gps.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gps.model.entity.MqttData;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * (MqttData)表数据库访问层
 *
 * @author zrx
 * @since 2024-05-20 13:49:57
 */
@Mapper
public interface MqttDataMapper extends BaseMapper<MqttData> {

	void insertBatchSomeColumn(@Param("list") List<MqttData> mqttDataList);

}
