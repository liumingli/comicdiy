package com.ybcx.comic.jobs;
/**
 * 在每次系统启动时,将库中内容同步的缓存中
 * @author liumingli
 *
 */
import org.apache.log4j.Logger;

import com.ybcx.comic.dao.DBAccessInterface;
import com.ybcx.comic.facade.AppStarter;


public class ServerStartSync implements Runnable{
		private Logger log = Logger.getLogger(ServerStartSync.class);
	
	    //由Spring注入
		private DBAccessInterface dbVisitor;

		// 同步开关
		private Boolean startFlag = true;
		
		public void run() {
			
			if(startFlag){
				AppStarter.labelList.clear();
				AppStarter.labelList = this.dbVisitor.getAllChildLabel();
				log.info("labelList size is "+AppStarter.labelList.size());
			}
		}

		public Boolean getStartFlag() {
			return startFlag;
		}

		public void setStartFlag(Boolean startFlag) {
			this.startFlag = startFlag;
		}

		public void setDbVisitor(DBAccessInterface dbVisitor) {
			this.dbVisitor = dbVisitor;
		}
		

}
