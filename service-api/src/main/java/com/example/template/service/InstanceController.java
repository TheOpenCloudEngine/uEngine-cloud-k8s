package com.example.template.service;

import com.example.template.model.InstanceModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/kube")
public class InstanceController {

    @Autowired
    InstanceService instanceService;

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


}
