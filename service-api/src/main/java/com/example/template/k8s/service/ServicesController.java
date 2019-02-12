package com.example.template.k8s.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.template.k8s.pod.Pod;
import com.example.template.k8s.service.K8sManagerService;

@RestController
@CrossOrigin("*")
@RequestMapping("/kube/v1/deployment")
public class ServicesController {

    @Autowired
    ServicesService deploymentService;

    @Autowired
    K8sManagerService k8sManagerService;

    @RequestMapping(value = "/instance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getAllInstance(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = deploymentService.getAllInstance();
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getAllInstanceByProvider(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable(value = "provider", required=false) String provider
    ) throws Exception {
        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = deploymentService.getAllInstanceByProvider(provider);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getInstanceByProviderAndName(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @PathVariable(value = "provider", required=false) String provider,
                                                        @PathVariable(value = "name", required=false) String name
    ) throws Exception {
        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = deploymentService.getInstanceByProviderAndName(provider, name);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

}
