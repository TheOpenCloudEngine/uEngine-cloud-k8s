package com.example.template.k8s.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.template.k8s.user.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kube/v1/service")
public class ServicesController {

    @Autowired
    ServicesService servicesService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getAllServices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         UserDetail userDetail
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = servicesService.getAllServices(userDetail);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getServicesByNamespace(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                                 UserDetail userDetail
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = servicesService.getServicesByNamespace(userDetail, namespace);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Services> getServicesByNamespaceName(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                         @PathVariable(value = "name") String name,
                                                     UserDetail userDetail
    ) throws Exception {

        List<Services> list = new ArrayList<Services>();
        Iterable<Services> it = servicesService.getServicesByNamespaceAndName(userDetail, namespace, name);
        for (Services item : it) {
            list.add(item);
        }

        return list;
    }

    /**
     * service create 요청
     */
    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createService(HttpServletRequest request,
                                            @RequestBody String body,
                                            @PathVariable(value = "namespace") String namespace,
                                            UserDetail userDetail
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();
        servicesService.createService(userDetail, namespace, body);

        return returnData;
    }
    /**
     * service update 요청
     */
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public Map<String,Object> updateService(
            @RequestBody String body,
            @PathVariable(value = "name") String name,
            @PathVariable(value = "namespace") String namespace,
            UserDetail userDetail
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();
        servicesService.updateService(userDetail, namespace, name, body);

        return returnData;
    }

    /**
     * service delete 요청
     */
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public Map<String,Object> deleteService(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable(value = "namespace") String namespace,
                                            @PathVariable(value = "name") String name,
                                            UserDetail userDetail
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        servicesService.deleteService(userDetail, namespace, name);

        return returnData;
    }

}
