package com.example.template.k8s.deployment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeploymentService {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentService.class);

    @Autowired
    DeploymentRepository deploymentRepository;

    @Cacheable(value="deployment" ,key="#deployment.id")
    public Deployment checkInstance(Deployment deployment){
        return deploymentRepository.findById(deployment.getId()).orElse(new Deployment());
    }

    @Cacheable(value="deployment")
    public Iterable<Deployment> getAllInstance(){
        return deploymentRepository.findAll();
    }

    @Cacheable(value="deployment", key="#provider")
    public Iterable<Deployment> getAllInstanceByProvider(String provider){
        return deploymentRepository.findByProvider(provider);
    }

    @Cacheable(value="deployment", key="#provider+#name")
    public Iterable<Deployment> getInstanceByProviderAndName(String provider, String name){
        return deploymentRepository.findByProviderAndName(provider,name);
    }


    public String update(Deployment deployment) {
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
