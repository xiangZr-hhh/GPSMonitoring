package com.gps.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gps.mapper.GpsDataMapper;
import com.gps.model.entity.GpsData;
import com.gps.utils.BaseResponse;
import com.gps.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @author zrx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("gps")
public class GpsController {

	@Autowired
	private GpsDataMapper gpsDataMapper;

	@GetMapping("getNowGpsData")
	public BaseResponse getNowGpsData() {
		LambdaQueryWrapper<GpsData> gpsDataLambdaQueryWrapper = new LambdaQueryWrapper<>();
		// 按时间戳字段倒序排列，即获取时间最近的一个数据
		gpsDataLambdaQueryWrapper.orderByDesc(GpsData::getCreatedTime);
		// 限制结果数量为1
		gpsDataLambdaQueryWrapper.last("LIMIT 1");

		GpsData gpsData = gpsDataMapper.selectOne(gpsDataLambdaQueryWrapper);
		if (gpsData != null) {
			return ResultUtil.success(gpsData);
		} else {
			return ResultUtil.success(null);
		}

	}


}


