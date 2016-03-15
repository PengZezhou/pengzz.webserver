package com.succez.server.connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.succez.server.util.Constant;

public class Connector {
	private static final Logger LOGGER = Logger.getLogger(Connector.class);
	public int requestMonitor(){
		LOGGER.info("开始监听请求");
		int flag = 0;
		ServerSocket sv = Constant.SERVER_SOCKET;
		while (!Constant.SHUTDOWN) {
			if (sv.isClosed() || Constant.THREAD_POOL.isShutdown()) {
				break;
			}
			Socket socket = null;
			try {
				socket = sv.accept();
				connectToEnd(socket);
			} catch (IOException e) {
				LOGGER.error("监听异常");
				flag = -1;
			}
		}
		return flag;
	}

	/**
	 * 将连接请求分发给线程池处理
	 * 
	 * @param socket
	 */
	private void connectToEnd(Socket socket) {
		// 多线程处理
		Constant.THREAD_POOL.execute(new ThreadPoolTask(socket));
	}

	/**
	 * 清理服务器资源
	 */
	public void resourceCleaner() {
		LOGGER.info("关闭线程池...");
		Constant.THREAD_POOL.shutdown();
		LOGGER.info("线程池已关闭");
		LOGGER.info("关闭serverSocket...");
		try {
			if (Constant.SERVER_SOCKET != null) {
				Constant.SERVER_SOCKET.close();
				LOGGER.info("serverSocket已关闭");
			}
		} catch (IOException e) {
			LOGGER.error("关闭serverSocket出现异常");
		}
	}
}
