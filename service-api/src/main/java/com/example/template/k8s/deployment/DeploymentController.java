package com.example.template.k8s.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin("*")
@RequestMapping("/kube/v1/deployment")
public class DeploymentController {

    @Autowired
    DeploymentService deploymentService;

    /**
     * deploy create 요청
     * @param body
     * @param namespace
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createDeployment(
                                               @RequestBody String body,
                                               @PathVariable(value = "namespace") String namespace
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();
        deploymentService.createDeploy(namespace, body);

        return returnData;
    }
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public Map<String,Object> updateDeployment(
                                               @RequestBody String body,
                                               @PathVariable(value = "name") String name,
                                               @PathVariable(value = "namespace") String namespace
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();
        deploymentService.updateDeploy(namespace, name, body);

        return returnData;
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Deployment> getAllDeployment(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<Deployment> list = new ArrayList<Deployment>();
        Iterable<Deployment> it = deploymentService.getAllDeployment();
        for (Deployment item : it) {
            list.add(item);
        }

        return list;
    }
    
    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Deployment> getDeploymentByNamespace(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace
    ) throws Exception {

        List<Deployment> list = new ArrayList<Deployment>();
        Iterable<Deployment> it = deploymentService.getDeploymentByNamespace(namespace);
        for (Deployment item : it) {
            list.add(item);
        }

        return list;
    }
    
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Deployment> getDeploymentByNamespaceName(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                         @PathVariable(value = "name") String name
    ) throws Exception {

        List<Deployment> list = new ArrayList<Deployment>();
        Iterable<Deployment> it = deploymentService.getDeploymentByNamespaceAndName(namespace, name);
        for (Deployment item : it) {
            list.add(item);
        }

        return list;
    }
    

    /**
     * deploy delete 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public Map<String,Object> deleteDeployment(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        deploymentService.deleteDeploy(namespace, name);

        return returnData;
    }

}
