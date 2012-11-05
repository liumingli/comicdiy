package com.ybcx.comic.jobs;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.ybcx.comic.dao.DBAccessInterface;
import com.ybcx.comic.facade.AppStarter;


public class CacheLabelTask extends TimerTask{
	
	private Logger log = Logger.getLogger(CacheLabelTask.class);
	
	private DBAccessInterface dbAccess;
	
	public CacheLabelTask(DBAccessInterface dbVisitor) {
		this.dbAccess = dbVisitor;
	}
	
	
	@Override
	public void run() {
		AppStarter.labelList.clear();
		AppStarter.labelList = this.dbAccess.getAllChildLabel();
		log.info("Midnight update labelList size is "+AppStarter.labelList.size());
	}

}
