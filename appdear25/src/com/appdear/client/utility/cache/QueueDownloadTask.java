package com.appdear.client.utility.cache;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class QueueDownloadTask<T>{
	private static QueueDownloadTask queuetask=new QueueDownloadTask();
	private QueueDownloadTask(){
		
	}
	public static QueueDownloadTask getInstance(){
		if(queuetask==null){
			queuetask=new QueueDownloadTask();
		}
		return queuetask;
	}
	private final static LinkedBlockingQueue queue=new LinkedBlockingQueue(100);
	
	public synchronized void pushQueue(T obj){
		Log.i("count","push="+queue.size());
		try {
			queue.offer(obj,10,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized T popQueue(){
		Log.i("count","pop="+queue.size());
		try {
			return (T)queue.poll(10,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized int getQueueCount(){
		Log.i("count","getQueueCount="+queue.size());
		return queue.size();
	}
}
