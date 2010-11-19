package org.jboss.lectures.auction.interceptors;

import java.io.Serializable;
import java.util.Date;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@LogMethodDuration
public class MethodDurationInterceptor implements Serializable 
{

	private static final long serialVersionUID = -8798435078202914597L;

	@AroundInvoke
	public Object measureDuration(InvocationContext ic) throws Exception 
	{
		
		long start = new Date().getTime();
		Object result = ic.proceed();
		long end = new Date().getTime();
		
		System.out.println("Method " + ic.getMethod().getName() + " took: " + (end - start) + " milliseconds");
		
		return result;
	}

}
