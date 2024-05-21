package com.gps.utils;

import com.gps.model.entity.GpsData;
import com.gps.model.entity.MqttData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 *
 * @author zrx
 */
@Slf4j
public class GpsDataUtil {

	/**
	 * mqtt转换
	 *
	 * @param mqttDataList
	 * @return java.util.List<com.gps.model.entity.GpsData>
	 * @author zrx
	 **/
	public static List<GpsData> mqttToGpsDataConversion(List<MqttData> mqttDataList) {
		List<GpsData> gpsDataList = new ArrayList<>();

		for (MqttData mqttData : mqttDataList) {
			String content = mqttData.getContent();
			try {
				List<String> gpsData = Arrays.asList(content.split(","));
				if (!gpsData.get(0).isEmpty() && (gpsData.get(0).startsWith("$") || Character.isLetter(gpsData.get(0).charAt(0)))&&
						gpsData.subList(1, gpsData.size()).stream().allMatch(data -> !data.isEmpty())) {
//					获取地理信息数据
					GpsData gpsDataResult = getGsonDataFromMqttData(gpsData,
							"GLL",3,1);
					if (gpsDataResult == null) {
						gpsDataResult = getGsonDataFromMqttData(gpsData,
								"GGA",4,2);
					}
					if (gpsDataResult == null) {
						gpsDataResult = getGsonDataFromMqttData(gpsData,
								"RMC",5,3);
					}
//					不为空，添加数据
					if (gpsDataResult != null) {
						log.info("\t\t>>从mqtt消息中获取到gps位置({},{})",
								gpsDataResult.getJingdu(),
								gpsDataResult.getWeidu());
						gpsDataList.add(gpsDataResult);
					}

				}
			} catch (Exception e) {
				log.info("\t>>转换异常: " + e.getMessage());
			}
		}
		return gpsDataList;
	}


	public static GpsData getGsonDataFromMqttData(List<String> content,
										   String headerName,
										   Integer jingdu,
										   Integer weidu) {
		if (content.get(0).contains(headerName)) {
			GpsData gpsDataEntityData = new GpsData();
			gpsDataEntityData.setGpsId(1L);

	// 获取原始经纬度的值
			BigDecimal lon = new BigDecimal(content.get(jingdu));
			BigDecimal lat = new BigDecimal(content.get(weidu));

	// 转换经纬度
			BigDecimal convertedLongitude = convertToDecimal(lon);
			BigDecimal convertedLatitude = convertToDecimal(lat);

			double[] lonAndLat = GpsCoordinateUtils.calWGS84toGCJ02(convertedLatitude.doubleValue(),convertedLongitude.doubleValue());

	// 设置BigDecimal值
			gpsDataEntityData.setJingdu(lonAndLat[1]); // 如果需要double类型，可以在需要时转换
			gpsDataEntityData.setWeidu(lonAndLat[0]);
			gpsDataEntityData.setCreatedTime(new Date());

			return gpsDataEntityData;
		}
		return null;
	}

	// 经纬度换算方法
	public static BigDecimal convertToDecimal(BigDecimal coordinate) {
		// 获取度数部分
		BigDecimal degrees = coordinate.divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN);
		// 获取分钟部分
		BigDecimal minutes = coordinate.remainder(BigDecimal.valueOf(100));
		// 将分钟部分转换为度
		BigDecimal decimal = degrees.add(minutes.divide(BigDecimal.valueOf(60), 6, RoundingMode.DOWN));
		decimal.subtract(BigDecimal.valueOf(0.00001));
		return decimal.setScale(6, RoundingMode.DOWN); // 保留小数点后六位，使用 DOWN 舍入模式
	}

}


