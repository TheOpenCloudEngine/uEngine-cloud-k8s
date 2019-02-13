package com.example.template.k8s.pod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.template.k8s.service.Services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/kube/v1/pods")
public class PodController {

    @Autowired
    PodService podService;

    /**
     * pod create 요청
     * @param request
     * @param response
     * @param namespace
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createPod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        podService.createPod(namespace, name);

        return returnData;
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getAllPods(HttpServletRequest request,
                                         HttpServletResponse response
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = podService.getAllPods();
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }
    
    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getPodsByNamespace(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = podService.getPodsByNamespace(namespace);
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }
    
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getPodsByNamespaceName(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                         @PathVariable(value = "name") String name
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = podService.getPodsByNamespaceAndName(namespace, name);
        for (Pod item : it) {
            list.add(item);
        }

        return list;
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
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public Map<String,Object> deletePod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        podService.deletePod(namespace, name);

        return returnData;
    }


}
