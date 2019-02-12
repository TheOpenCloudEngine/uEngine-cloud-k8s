package com.example.template.k8s.pod;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.template.k8s.service.K8sManagerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/kube/v1")
public class PodController {

    @Autowired
    PodService instanceService;

    @Autowired
    K8sManagerService k8sManagerService;

    @RequestMapping(value = "/instance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getAllInstance(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = instanceService.getAllInstance();
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getAllInstanceByProvider(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable(value = "provider", required=false) String provider
    ) throws Exception {
        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = instanceService.getAllInstanceByProvider(provider);
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/instance/{provider}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getInstanceByProviderAndName(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @PathVariable(value = "provider", required=false) String provider,
                                                        @PathVariable(value = "name", required=false) String name
    ) throws Exception {
        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = instanceService.getInstanceByProviderAndName(provider, name);
        for (Pod item : it) {
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
     * deploy create 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/deployments/{name}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public Map<String,Object> deleteDeployment(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        k8sManagerService.deleteDeploy(namespace, name);

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
    /**
     * pod delete 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/pods/{name}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public Map<String,Object> deletePod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        k8sManagerService.deletePod(namespace, name);

        return returnData;
    }


}
