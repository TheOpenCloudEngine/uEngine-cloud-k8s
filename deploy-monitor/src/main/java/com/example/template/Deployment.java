package com.example.template;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class Deployment implements Serializable {

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
	private Integer specReplicas;
	private String strategyType;
	private String Status;
	private Integer statusReplicas;
	private Integer statusAvailableReplicas;
	private Integer statusReadyReplicas;
	private Integer statusUpdateReplicas;
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

	public Integer getSpecReplicas() {
		return specReplicas;
	}

	public void setSpecReplicas(Integer specReplicas) {
		this.specReplicas = specReplicas;
	}

	public String getStrategyType() {
		return strategyType;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public void setStrategyType(String strategyType) {
		this.strategyType = strategyType;
	}

	public Integer getStatusReplicas() {
		return statusReplicas;
	}

	public void setStatusReplicas(Integer statusReplicas) {
		this.statusReplicas = statusReplicas;
	}

	public Integer getStatusAvailableReplicas() {
		return statusAvailableReplicas;
	}

	public void setStatusAvailableReplicas(Integer statusAvailableReplicas) {
		this.statusAvailableReplicas = statusAvailableReplicas;
	}

	public Integer getStatusReadyReplicas() {
		return statusReadyReplicas;
	}

	public void setStatusReadyReplicas(Integer statusReadyReplicas) {
		this.statusReadyReplicas = statusReadyReplicas;
	}

	public Integer getStatusUpdateReplicas() {
		return statusUpdateReplicas;
	}

	public void setStatusUpdateReplicas(Integer statusUpdateReplicas) {
		this.statusUpdateReplicas = statusUpdateReplicas;
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

