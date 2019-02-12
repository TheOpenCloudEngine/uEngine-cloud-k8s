package com.example.template;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class Services implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String provider;
	private String id;
	private String type;
	private String apiVersion;
	private String kind;
	private String createTimeStamp;
	private String name;
	private String namespace;
	private String uid;
	private String specClusterIp;
	private Integer specPort;
	private String specProtocol;
	private Integer specTargetPort;
	private String specSessionAffinity;
	private String specType;
	private String hostname;
	private String ingressIp;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(String createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSpecClusterIp() {
		return specClusterIp;
	}

	public void setSpecClusterIp(String specClusterIp) {
		this.specClusterIp = specClusterIp;
	}

	public Integer getSpecPort() {
		return specPort;
	}

	public void setSpecPort(Integer specPort) {
		this.specPort = specPort;
	}

	public String getSpecProtocol() {
		return specProtocol;
	}

	public void setSpecProtocol(String specProtocol) {
		this.specProtocol = specProtocol;
	}

	public Integer getSpecTargetPort() {
		return specTargetPort;
	}

	public void setSpecTargetPort(Integer specTargetPort) {
		this.specTargetPort = specTargetPort;
	}

	public String getSpecSessionAffinity() {
		return specSessionAffinity;
	}

	public void setSpecSessionAffinity(String specSessionAffinity) {
		this.specSessionAffinity = specSessionAffinity;
	}

	public String getSpecType() {
		return specType;
	}

	public void setSpecType(String specType) {
		this.specType = specType;
	}
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIngressIp() {
		return ingressIp;
	}

	public void setIngressIp(String ingressIp) {
		this.ingressIp = ingressIp;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}

