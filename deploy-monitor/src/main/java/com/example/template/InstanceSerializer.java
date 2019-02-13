package com.example.template;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.reflect.TypeToken;

import io.kubernetes.client.JSON;

import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.util.Watch;

public class InstanceSerializer implements Serializer {

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        byte[] retVal = null;
        
//        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
//        yamlReader.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//        yamlReader.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        yamlReader.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
//        
//        try {
//        	retVal = yamlReader.writeValueAsBytes(data.toString());
//        	yamlReader.writer().writeValueAsBytes(data.toString().getBytes())
//        	yamlReader.readValue(yamlReader.writeValueAsBytes(data.toString()), V1Deployment.class)
//		} catch (JsonProcessingException e1) {
//			e1.printStackTrace();
//		}
        
        if(retVal == null)
        {
        	ObjectMapper objectMapper = new ObjectMapper();
            try {
                retVal = objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return retVal;

    }

    @Override
    public void close() {

    }
}
