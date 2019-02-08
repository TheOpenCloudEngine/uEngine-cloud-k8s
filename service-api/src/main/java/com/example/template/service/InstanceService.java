package com.example.template.service;

import com.example.template.model.InstanceModel;
import com.example.template.model.InstanceModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class InstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(InstanceService.class);

    @Autowired
    InstanceModelRepository instanceModelRepository;

    @Cacheable(value="instance" ,key="#instance.id")
    public InstanceModel checkInstance(InstanceModel instance){
        return instanceModelRepository.findById(instance.getId()).orElse(new InstanceModel());
    }

    @Cacheable(value="instance")
    public Iterable<InstanceModel> getAllInstance(){
        LOG.info("use getAllInstance database");
        return instanceModelRepository.findAll();
    }

    @Cacheable(value="instance", key="#provider")
    public Iterable<InstanceModel> getAllInstanceByProvider(String provider){
        LOG.info("use getAllInstanceByProvider database");
        return instanceModelRepository.findByProvider(provider);
    }

    @Cacheable(value="instance", key="#provider+#name")
    public Iterable<InstanceModel> getInstanceByProviderAndName(String provider, String name){
        LOG.info("use getAllInstanceByProvider database");
        return instanceModelRepository.findByProviderAndName(provider,name);
    }


    /**
     * 단건을 업데이트 한다
     */
    @Caching(put = {
            @CachePut(value = "instance", key="#instance.id")
    })
    public String update(InstanceModel instance) {
//        LOG.info("cache update .. {}", instance.toString());
        return "";
    }

    /**
     * 관련된 캐쉬를 지운다,
     */
    @Caching(evict = {
            @CacheEvict(value = "instance"),
            @CacheEvict(value = "instance", key="#instance.id"),
            @CacheEvict(value = "instance", key ="#instance.provider"),
            @CacheEvict(value = "instance", key ="#instance.provider+#instance.name")
    })
    public String deleteCacheList(InstanceModel instance) {
//        LOG.info("cache delete .. {}", instance.toString());
        return "";
    }

}
