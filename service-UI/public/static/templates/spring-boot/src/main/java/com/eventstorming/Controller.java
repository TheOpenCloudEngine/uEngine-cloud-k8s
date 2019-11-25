forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

 @RestController
 public class {{ namePascalCase }}Controller {

        {{#commands}}
        {{name}}
        @RequestMapping(value = /{{aggregate.nameCamelCase}}/{{nameCamelCase}}/, method = RequestMethod.{{restfulType}}, produces = "application/json;charset=UTF-8")
           public void {{nameCamelCase}}(HttpServletRequest request, HttpServletResponse response) throws Exception {

        }
        {{/commands}}
 }
