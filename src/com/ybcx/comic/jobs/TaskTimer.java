package com.ybcx.comic.jobs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class TaskTimer {

	private final Timer timer = new Timer(true);
	
	private int min = 1;
	
	// 一天的毫秒数
	long daySpan = 24 * 60 * 60 * 1000;

	
	private CacheLabelTask midnightTask;

	public TaskTimer() {
		
	}


	public CacheLabelTask getMidnightTask() {
		return midnightTask;
	}

	public void setMidnightTask(CacheLabelTask midnightTask) {
		this.midnightTask = midnightTask;
	}

	public int getMin() {
		return min;
	}


	public void setMin(int min) {
		this.min = min;
	}


	public void start() {
		
	}

	public void runAtFixTime(String timeToRun) {
		// 规定的每天时间00:00:00运行
		// final SimpleDateFormat dateFormat = new
		// SimpleDateFormat("yyyy-MM-dd '00:00:00'");
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd '"
				+ timeToRun + "'");

		// 首次运行时间
		Date startTime = null;
		try {
			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(dateFormat.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 如果今天的已经过了 首次运行时间就改为明天
		if (System.currentTimeMillis() > startTime.getTime())
			startTime = new Date(startTime.getTime() + daySpan);

		// 以每24小时执行一次
		timer.scheduleAtFixedRate(midnightTask, startTime, daySpan);
	}

	public void stop() {
		timer.cancel();
	}

}
