forEach: Aggregate
fileName: {{namePascalCase}}.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{namePascalCase}}table")
public class {{namePascalCase}} {

    {{#aggregateRoot.fieldDescriptors}}
    {{#isKey}}
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    {{/isKey}}
    private {{className}} {{nameCamelCase}};
    {{/aggregateRoot.fieldDescriptors}}

    {{#aggregateRoot.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
    {{/aggregateRoot.fieldDescriptors}}


{{#events}}
    {{trigger}}
    public void publish{{namePascalCase}}(@Payload String message,ConsumerRecord<?, ?> consumerRecord)
        {
            System.out.println("##### listener : "+message);
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

            {{namePascalCase}} {{nameCamelCase}} = null;

            try{
                {{nameCamelCase}}=objectMapper.readValue( message,{{namePascalCase}}.class );
                System.out.println(" #### type = "+{{name}}.getEventType());

                if( {{nameCamelCase}}.getEventType()!=null &&
                    {{nameCamelCase}}.getEventType().equals( {{namePascalCase}}.class.getSimpleName()) )
                    {
                        // TO-DO :: Implement your Logic here.
                    }
                }catch(Exception e){

             }
        }
{{/events}}

}
