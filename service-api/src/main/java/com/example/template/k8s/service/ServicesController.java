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

@RestController
@CrossOrigin("*")
@RequestMapping("/kube/v1/service")
public class ServicesController {

    @Autowired
    ServicesService servicesService;

    /**
     * service create 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createService(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable(value = "namespace") String namespace,
                                            @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        servicesService.createService(namespace, name);

        return returnData;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getAllServices(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = servicesService.getAllServices();
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }
    
    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getServicesByNamespace(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = servicesService.getServicesByNamespace(namespace);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }
    
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getServicesByNamespaceName(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                         @PathVariable(value = "name") String name
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = servicesService.getServicesByNamespaceAndName(namespace, name);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

}