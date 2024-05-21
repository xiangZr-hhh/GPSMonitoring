package com.gps.model.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * (MqttData)表实体类
 *
 * @author zrx
 * @since 2024-05-20 13:49:57
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("mqtt_data")
public class MqttData  {
    @TableId(type= IdType.AUTO)
    private Long id;
    //主题
    private String topic;
    //内容
    private String content;
    //创建时间
    private Date createdTime;


}
