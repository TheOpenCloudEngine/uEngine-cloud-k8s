package com.example.template.k8s.pod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Entity
@Table(name = "POD")
public class Pod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name="provider")
    private String provider;
    
    @Column(name="type")
    private String type;

    @Column(name="kind")
    private String kind;

    @Column(name="name")
    private String name;

    @Column(name="namespace")
    private String namespace;

    @Column(name="uid")
    private String uid;

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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
    
}

