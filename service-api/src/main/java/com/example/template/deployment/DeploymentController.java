package com.example.template.controller;

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

import com.example.template.model.Deployment;
import com.example.template.model.InstanceModel;
import com.example.template.service.DeploymentService;
import com.example.template.service.K8sManagerService;

@RestController
@CrossOrigin("*")
@RequestMapping("/kube/v1/deployment")
public class DeploymentController {

    @Autowired
    DeploymentService deploymentService;

    @Autowired
    K8sManagerService k8sManagerService;

    @RequestMapping(value = "/instance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Deployment> getAllInstance(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<Deployment> list = new ArrayList<Deployment>();
        Iterable<Deployment> it = deploymentService.getAllInstance();
        for (Deployment item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Deployment> getAllInstanceByProvider(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable(value = "provider", required=false) String provider
    ) throws Exception {
        List<Deployment> list = new ArrayList<Deployment>();
        Iterable<Deployment> it = deploymentService.getAllInstanceByProvider(provider);
        for (Deployment item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Deployment> getInstanceByProviderAndName(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @PathVariable(value = "provider", required=false) String provider,
                                                        @PathVariable(value = "name", required=false) String name
    ) throws Exception {
        List<Deployment> list = new ArrayList<Deployment>();
        Iterable<Deployment> it = deploymentService.getInstanceByProviderAndName(provider, name);
        for (Deployment item : it) {
            list.add(item);
        }

        return list;
    }

}
