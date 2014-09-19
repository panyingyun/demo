package com.kl.klservice;

public interface IKL {
	public void create(int sdkversion);

	public void destory();

	public void stopself();

	public void register(String pkg, byte sdktype);

	public void receiver(String pkg, byte sdktype);
}
