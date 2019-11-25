forEach: Aggregate
fileName: {{namePascalCase}}EventListner.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
public class {{namePascalCase}}EventListener{

@Autowired
private {{namePascalCase}}Repository {{namePascalCase}}Repository;

{{#policies}}
        {{#relationEventInfo}}
@KafkaListener(topics = "${eventTopic}", groupId = "{{boundedContext.name}}")
public void on{{relationEventInfo.namePascalCase}}(){
        //event emmit code
        System.out.println("##### listener : "+message);

        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        {{relationEventInfo.namePascalCase}} {{relationEventInfo.name}}=null;

            try{
                {{relationEventInfo.name}}=objectMapper.readValue(message,{{relationEventInfo.namePascalCase}}.class);

                if({{relationEventInfo.name}}.getEventType().equals({{relationEventInfo.namePascalCase}}.class.getSimpleName())){

                }

            }catch(Exception e){

            }
        }

        {{/relationEventInfo}}
{{/policies}}
}
