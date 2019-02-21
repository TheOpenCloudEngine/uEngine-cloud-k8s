package com.example.template.k8s.deployment;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
@Entity
@Table(name = "DEPLOYMENT")
public class Deployment implements Serializable {

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
	
	@Column(name="specReplicas")
	private Integer specReplicas;
	
	@Column(name="strategyType")
	private String strategyType;

	@Column(name="statusReplicas")
	private Integer statusReplicas;
	
	@Column(name="statusAvailableReplicas")
	private Integer statusAvailableReplicas;
	
	@Column(name="statusReadyReplicas")
	private Integer statusReadyReplicas;
	
	@Column(name="statusUpdateReplicas")
	private Integer statusUpdateReplicas;
	
	@Column(name="createTime", length=6)
	private Timestamp createTime;
	
	@Column(name="updateTime", length=6)
	private Timestamp updateTime;
	
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

	public Integer getSpecReplicas() {
		return specReplicas;
	}

	public void setSpecReplicas(Integer specReplicas) {
		this.specReplicas = specReplicas;
	}

	public String getStrategyType() {
		return strategyType;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
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

