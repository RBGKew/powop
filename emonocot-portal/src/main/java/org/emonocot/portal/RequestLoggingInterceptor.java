package org.emonocot.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		request.setAttribute("startTime", System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		request.setAttribute("processTime", System.currentTimeMillis());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
		long startTime = (Long) request.getAttribute("startTime");
		long preRenderTime = (Long) request.getAttribute("processTime");
		long currentTime = System.currentTimeMillis();
		StringBuilder str = new StringBuilder();
		str.append("path: ");
		str.append(request.getRequestURI());

		str.append(" | query: ");
		str.append(request.getQueryString());

		str.append(" | duration: ");
		str.append(currentTime - startTime);
		str.append("ms");

		str.append(" [proc: ");
		str.append(preRenderTime - startTime);
		str.append("ms, view: ");
		str.append(currentTime - preRenderTime);
		str.append("ms]");

		logger.info(str.toString());
	}
}
