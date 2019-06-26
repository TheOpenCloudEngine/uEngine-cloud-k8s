package com.example.template.List;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "List")
public class List implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      id = id;
   }

 String pro;
   public String getpro() {
      return pro;
   }

   public void setpro(String pro) {
      this.pro = pro;
   }

 int prod;
   public int getprod() {
      return prod;
   }

   public void setprod(int prod) {
      this.prod = prod;
   }

 String pro;
   public String getpro() {
      return pro;
   }

   public void setpro(String pro) {
      this.pro = pro;
   }

 int prod;
   public int getprod() {
      return prod;
   }

   public void setprod(int prod) {
      this.prod = prod;
   }