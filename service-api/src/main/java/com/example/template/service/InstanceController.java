package com.example.template.service;

import com.example.template.model.InstanceModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/kube/v1")
public class InstanceController {

    @Autowired
    InstanceService instanceService;

    @Autowired
    K8sManagerService k8sManagerService;

    @RequestMapping(value = "/instance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<InstanceModel> getAllInstance(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<InstanceModel> list = new ArrayList<InstanceModel>();
        Iterable<InstanceModel> it = instanceService.getAllInstance();
        for (InstanceModel item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<InstanceModel> getAllInstanceByProvider(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable(value = "provider", required=false) String provider
    ) throws Exception {
        List<InstanceModel> list = new ArrayList<InstanceModel>();
        Iterable<InstanceModel> it = instanceService.getAllInstanceByProvider(provider);
        for (InstanceModel item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<InstanceModel> getInstanceByProviderAndName(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @PathVariable(value = "provider", required=false) String provider,
                                                        @PathVariable(value = "name", required=false) String name
    ) throws Exception {
        List<InstanceModel> list = new ArrayList<InstanceModel>();
        Iterable<InstanceModel> it = instanceService.getInstanceByProviderAndName(provider, name);
        for (InstanceModel item : it) {
            list.add(item);
        }

        return list;
    }

    /**
     * service create 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/service/{name}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createService(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        k8sManagerService.createService(namespace, name);

        return returnData;
    }

    /**
     * deploy create 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/deployments/{name}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createDeployment(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        k8sManagerService.createDeploy(namespace, name);

        return returnData;
    }

    /**
     * pod create 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/pods/{name}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createPod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        k8sManagerService.createPod(namespace, name);

        return returnData;
    }


}
