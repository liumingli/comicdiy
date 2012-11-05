package com.ybcx.comic.jobs;


public class LabelToCache {
	private Thread threader;
	
	private ServerStartSync worker;
	
	public LabelToCache() {
		
	}
	
	public void start(){
		if(threader!=null  && !threader.isAlive()) {
			threader.start();
		}
	}
	
	public void stop(){
		if(threader==null) worker.setStartFlag(false);
	}

	public ServerStartSync getExecutor() {
		return worker;
	}

	//由Spring注入
	public void setExecutor(ServerStartSync worker) {
		this.worker = worker;	
		threader = new Thread(worker);
	}

}
