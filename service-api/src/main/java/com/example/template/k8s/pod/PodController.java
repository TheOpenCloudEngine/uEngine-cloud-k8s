package com.example.template.k8s.pod;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.template.k8s.user.UserDetail;

@RestController
@RequestMapping("/kube/v1/pods")
public class PodController {

    @Autowired
    PodService podService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getAllPods(HttpServletRequest request,
                                HttpServletResponse response,
                                UserDetail userDetail
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = podService.getAllPods(userDetail);
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/namespaces", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<String> getAllNamespaces(HttpServletRequest request,
                                         HttpServletResponse response,
                                        UserDetail userDetail
    ) throws Exception {

        return podService.getAllNamespaces(userDetail);
    }

    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getPodsByNamespace(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                        UserDetail userDetail
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = podService.getPodsByNamespace(userDetail, namespace);
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }

    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Pod> getPodsByNamespaceName(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable(value = "namespace") String namespace,
                                         @PathVariable(value = "name") String name,
                                         UserDetail userDetail
    ) throws Exception {

        List<Pod> list = new ArrayList<Pod>();
        Iterable<Pod> it = podService.getPodsByNamespaceAndName(userDetail, namespace, name);
        for (Pod item : it) {
            list.add(item);
        }

        return list;
    }

    /**
     * pod create 요청
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/namespaces/{namespace}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> createPod(HttpServletRequest request,
                                        @RequestBody String body,
                                        @PathVariable(value = "namespace") String namespace,
                                        UserDetail userDetail
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();
        podService.createPod(userDetail, namespace, body);

        return returnData;
    }

    /**
     * pod update 요청
     */
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public Map<String,Object> updatePod(
            @RequestBody String body,
            @PathVariable(value = "name") String name,
            @PathVariable(value = "namespace") String namespace,
            UserDetail userDetail
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();
        podService.updatePod(userDetail, namespace, name, body);

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
    @RequestMapping(value = "/namespaces/{namespace}/{name}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public Map<String,Object> deletePod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "name") String name,
                                               UserDetail userDetail
    ) throws Exception {
        Map<String,Object> returnData = new HashMap<String,Object>();

        podService.deletePod(userDetail, namespace, name);

        return returnData;
    }


    @RequestMapping(value = "/namespaces/{namespace}/pods/{name}/log", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ArrayList<LogMessageFormat> getPodsByNamespace(
    			HttpServletRequest request,
    			@RequestParam String username,
    			@PathVariable(value = "namespace") String namespace,
    			@PathVariable(value = "name") String name
    ) throws Exception {

        return podService.getLog(username, namespace, name);
    }

    @RequestMapping(value = "/namespaces/{namespace}/pods/{name}/desc", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ArrayList<LogMessageFormat> getDescPodsByNamespace(
            HttpServletRequest request,
            @RequestParam String username,
            @PathVariable(value = "namespace") String namespace,
            @PathVariable(value = "name") String name
    ) throws Exception {

        return podService.getLog(username, namespace, name);
    }
}
