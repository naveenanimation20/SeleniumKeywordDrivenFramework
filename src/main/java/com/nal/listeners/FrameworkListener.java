package com.nal.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

//custom listener
public class FrameworkListener implements ITestListener{
	
	
	@Override
	public synchronized void onStart(ITestContext context) {
		System.out.println("Test Suite started!");
		
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		System.out.println(("Test Suite is ending!"));
		
	}

	@Override
	public synchronized void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);
		
		System.out.println("test method started : " + className + " : " + methodName);
	
	}

	public synchronized void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println((methodName + " passed!"));
		
	}

	public synchronized void onTestFailure(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " failed!");
		
	}

	public synchronized void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " skipped!");
	}
	
	
	

}
