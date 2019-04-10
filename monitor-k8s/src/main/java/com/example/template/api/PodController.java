package com.example.template.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin("*")
@RequestMapping("/api/v1")
public class PodController {

    @Autowired
    PodService podService;

    
    @RequestMapping(value = "/namespaces/{namespace}/pods/{name}/log", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ArrayList<LogMessageFormat> getPodsByNamespace(
    			@RequestHeader(value="kubehost") String kubehost,
    			@RequestHeader(value="kubetoken") String kubetoken,
    			@PathVariable(value = "namespace") String namespace,
    			@PathVariable(value = "name") String name
    ) throws Exception {

        return podService.getLog(kubehost, kubetoken, namespace, name);
    }
    


}
