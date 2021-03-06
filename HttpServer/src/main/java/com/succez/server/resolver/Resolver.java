package com.succez.server.resolver;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.succez.server.analysis.Analysis;
import com.succez.server.analysis.FileConfig;
import com.succez.server.http.Request;
import com.succez.server.util.Constant;

/**
 * 解析器
 * 
 * @author Peng.Zezhou
 *
 */
public class Resolver {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Resolver.class);
	private Socket socket;

	/**
	 * 解析客户连接
	 * 
	 * @param socket
	 */
	public Resolver(Socket socket) {
		this.socket = socket;
		urlResolve();
	}

	/**
	 * 解析出url请求信息
	 */
	private void urlResolve() {
		LOGGER.info("开始解析url");
		try {
			Request r = new Request();
			r.bind(socket);
			String uri = URLDecoder.decode(r.getUrl(), Constant.SERVER_ENCODE);
			LOGGER.info("url " + uri);
			if (!r.getType().equals("GET")) {
				uri = System.getProperty("user.dir")
						+ FileConfig.getInstance().getNot_support();
			}
			String range = r.getHead().get("Range");
			new Analysis(this.socket, uri, range);
		} catch (IOException e) {
			LOGGER.info("url解析异常，IO异常");
		} finally {
		}
	}
}
