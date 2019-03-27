package com.example.template.k8s.pod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "POD")
public class Pod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name="provider")
    private String provider;

	@Column(name="apiVersion")
	private String apiVersion;

	@Column(name="kind")
	private String kind;
    
    @Column(name="host")
    private String host;

    @Column(name="name")
    private String name;

    @Column(name="namespace")
    private String namespace;

    @Column(name="createTimeStamp")
    private String createTimeStamp;

    @Column(name="image")
    private String image;

    @Column(name="nodeName")
    private String nodeName;

    @Column(name="restartPolicy")
    private String restartPolicy;

    @Column(name="serviceAccount")
    private String serviceAccount;
    
    @Column(name="hostIp")
    private String hostIp;

    @Column(name="podIp")
    private String podIp;
    
    @Column(name="status")
    private String status;
    
    @Column(name="createTime", length=6)
	private Timestamp createTime;
	
	@Column(name="updateTime", length=6)
	private Timestamp updateTime;
	
	@Column(name="sourceData", columnDefinition = "TEXT")
	private String sourceData;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public String getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(String createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getRestartPolicy() {
		return restartPolicy;
	}

	public void setRestartPolicy(String restartPolicy) {
		this.restartPolicy = restartPolicy;
	}

	public String getServiceAccount() {
		return serviceAccount;
	}

	public void setServiceAccount(String serviceAccount) {
		this.serviceAccount = serviceAccount;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getPodIp() {
		return podIp;
	}

	public void setPodIp(String podIp) {
		this.podIp = podIp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

