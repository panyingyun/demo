package com.cl.clservice.core;

public class PkgInfo {
	public String pkg;
	public byte sdktype;
	public String channel;

	public PkgInfo(String pkg, byte sdktype) {
		this.pkg = pkg;
		this.sdktype = sdktype;
		this.channel = "";
	}
	
	public PkgInfo(String pkg, byte sdktype, String channel) {
		this.pkg = pkg;
		this.sdktype = sdktype;
		this.channel = channel;
	}
}
