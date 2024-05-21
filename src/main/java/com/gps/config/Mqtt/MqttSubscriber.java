package com.gps.config.Mqtt;

import com.gps.dao.MqttDAO;
import com.gps.mapper.GpsDataMapper;
import com.gps.mapper.MqttDataMapper;
import com.gps.model.entity.GpsData;
import com.gps.model.entity.MqttData;
import com.gps.utils.GpsDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqttSubscriber {

    private final Environment environment;

    @Autowired
    private  MqttDataMapper mqttDataMapper;
    @Autowired
    private GpsDataMapper gpsDataMapper;

    private final int batchSize = 20; // 批量插入阈值
    private List<MqttData> mqttDataList = new ArrayList<>(); // 存储 MQTT 数据的列表


//    启动mqtt连接
    @Bean
    public void startMqttSubscriber() {
        // 设置mqtt服务器地址与uid
        String mqttAddress = environment.getProperty("mqtt.address");
        String clientId = "linux_server";
        String topic = "gps_data_topic";
        int qos = 0;

        try {
            MqttClient client = new MqttClient(mqttAddress, clientId, new MemoryPersistence());
            // 连接参数(超时时间与心跳)
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);

            // 设置回调
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    log.error("连接丢失: {}", cause.getMessage());
                }

                public void messageArrived(String topic, MqttMessage message) {
                    String content = new String(message.getPayload());
//                    不添加空数据
                    if (content.isEmpty()) {
                        return;
                    }
                    MqttData mqttData = new MqttData();
                    mqttData.setContent(content);
                    mqttData.setTopic(topic);
                    mqttData.setCreatedTime(new Date());
                    mqttDataList.add(mqttData);
                    // 当数据列表达到阈值时，进行批量插入并清空列表
                    if (mqttDataList.size() >= batchSize) {
                        insertGpsData();
                        batchInsertAndClear();
                    }
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("接收完成: {}", token.isComplete());
                }
            });

            client.connect(options);
            client.subscribe(topic, qos);
            log.info("MQTT 连接成功.");
        } catch (Exception e) {
            log.error("MQTT 连接异常: {}", e.getMessage());
        }
    }

    // 批量插入数据并清空列表
    private void batchInsertAndClear() {
        mqttDataMapper.insertBatchSomeColumn(mqttDataList); // 批量插入数据库
        mqttDataList.clear(); // 清空数据列表
        log.info("\t\t>>批量插入mqtt消息数据{}条",batchSize);
    }

    //  批量插入gps数据
    public void insertGpsData() {
       List<GpsData> gpsData = GpsDataUtil.mqttToGpsDataConversion(mqttDataList);
       if (!gpsData.isEmpty()) {
//           去重
            gpsData = gpsData.stream()
                    .collect(Collectors.toMap(
                            data -> data.getWeidu() + "-" + data.getJingdu(), // 根据经纬度生成key
                            Function.identity(),
                            (existing, replacement) -> existing // 如果key已存在，则保留原始数据
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
//            批量添加
           gpsDataMapper.insertBatchSomeColumn(gpsData);
       }
       log.info("\t\t>>批量插入gps数据");
    }


}