package me.bleidi.devfestcerrado;

public class Visitor {

	private final String ip;
	private final String userAgent;
	
	public Visitor(String ip, String userAgent) {
		this.ip = ip;
		this.userAgent = userAgent;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public String getUserAgent() {
		return this.userAgent;
	}
}
