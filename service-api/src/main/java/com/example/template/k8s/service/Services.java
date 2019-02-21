package com.example.template.k8s.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import lombok.Data;

@Data
@Entity
@Table(name = "SERVICES")
public class Services implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="provider")
	private String provider;

	@Id
	private String id;
	
	@Column(name="type")
	private String type;
	
	@Column(name="apiVersion")
	private String apiVersion;
	
	@Column(name="kind")
	private String kind;
	
	@Column(name="createTimeStamp")
	private String createTimeStamp;
	
	@Column(name="name")
	private String name;
	
	@Column(name="namespace")
	private String namespace;
	
	@Column(name="uid")
	private String uid;
	
	@Column(name="specClusterIp")
	private String specClusterIp;
	
	@Column(name="specPort")
	private Integer specPort;
	
	@Column(name="specProtocol")
	private String specProtocol;
	
//	@Column(name="specTargetPort")
//	private String specTargetPort;
	
	@Column(name="specSessionAffinity")
	private String specSessionAffinity;
	
	@Column(name="specType")
	private String specType;
	
	@Column(name="hostname")
	private String hostname;
	
	@Column(name="ingressIp")
	private String ingressIp;
	
	@Column(name="createTime", length=6)
	private DateTime createTime;
	
	@Column(name="updateTime", length=6)
	private DateTime updateTime;
	
	@Column(name="sourceData", columnDefinition = "TEXT")
	private String sourceData;

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

//	public String getSpecTargetPort() {
//		return specTargetPort;
//	}
//
//	public void setSpecTargetPort(String specTargetPort) {
//		this.specTargetPort = specTargetPort;
//	}

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
	
	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}

	public DateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getSourceData() {
		return sourceData;
	}

	public void setSourceData(String sourceData) {
		this.sourceData = sourceData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}

