package com.gps.dao;

import com.gps.mapper.MqttDataMapper;
import com.gps.model.entity.MqttData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述：
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqttDAO {

	private final MqttDataMapper mqttDataMapper;

	public void InsertData (MqttData mqttData) {
		mqttDataMapper.insert(mqttData);
	}

}


