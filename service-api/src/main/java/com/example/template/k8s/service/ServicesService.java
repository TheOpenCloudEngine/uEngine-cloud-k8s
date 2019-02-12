package com.example.template.k8s.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ServicesService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesService.class);

    @Autowired
    ServicesRepository deploymentRepository;

    @Cacheable(value="deployment" ,key="#deployment.id")
    public Services checkInstance(Services deployment){
        return deploymentRepository.findById(deployment.getId()).orElse(new Services());
    }

    @Cacheable(value="deployment")
    public Iterable<Services> getAllInstance(){
        return deploymentRepository.findAll();
    }

    @Cacheable(value="deployment", key="#provider")
    public Iterable<Services> getAllInstanceByProvider(String provider){
        return deploymentRepository.findByProvider(provider);
    }

    @Cacheable(value="deployment", key="#provider+#name")
    public Iterable<Services> getInstanceByProviderAndName(String provider, String name){
        return deploymentRepository.findByProviderAndName(provider,name);
    }


    public String update(Services deployment) {
    	deploymentRepository.save(deployment);
        return "";
    }

    /**
     * 관련된 캐쉬를 지운다,
     */
//    @Caching(evict = {
//            @CacheEvict(value = "instance"),
//            @CacheEvict(value = "instance", key="#instance.id"),
//            @CacheEvict(value = "instance", key ="#instance.provider"),
//            @CacheEvict(value = "instance", key ="#instance.provider+#instance.name")
//    })
//    public String deleteCacheList(InstanceModel instance) {
//        return "";
//    }

}
