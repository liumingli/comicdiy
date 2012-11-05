package com.ybcx.comic.jobs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.ybcx.comic.dao.DBAccessInterface;


/**
 * Application Lifecycle Listener implementation class AppStarter
 * 用来启动应用后，开启自动计算任务
 */
public class TaskStarter  {

	private TaskTimer fixRunTimer;
	
	//由Spring注入
	private DBAccessInterface dbVisitor;

	
	private Logger log = Logger.getLogger(TaskStarter.class);

	
    public TaskStarter() {
        // DO NOTHING...    	
    }

	
	// Spring injection
	public void setDbVisitor(DBAccessInterface dbVisitor) {
		this.dbVisitor = dbVisitor;
	}
	
	
    public void runAutoTasks() {
    	
    	String status = "AppStarer listener running...";
    	log.debug(status);
    	
    	//固定任务用清除缓存中的一些数据，比如点击量等(待完善)
    	fixRunTimer = new TaskTimer();
    	fixRunTimer.setMidnightTask(new CacheLabelTask(dbVisitor));
    	//每天0点运行
    	fixRunTimer.runAtFixTime("00:00:00");

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void stopTask() {
    	
    	String status = "AppStarer listener stopped!";
    	log.debug(status);
    	
    	fixRunTimer.stop();
    }
	
}
