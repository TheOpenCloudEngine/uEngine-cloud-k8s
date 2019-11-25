forEach: Aggregate
fileName: {{namePascalCase}}.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{name}}table")
public class {{namePascalCase}} {

    {{#aggregateRoot.fieldDescriptors}}
    {{#isKey}}
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    {{/isKey}}
    private {{className}} {{name}};
    {{/aggregateRoot.fieldDescriptors}}

    {{#aggregateRoot.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{name}};
    }

    public void set{{namePascalCase}}({{className}} {{name}}) {
        this.{{name}} = {{name}};
    }
    {{/aggregateRoot.fieldDescriptors}}


{{#events}}
    {{trigger}}
    public void publish{{namePascalCase}}(@Payload String message,ConsumerRecord<?, ?> consumerRecord)
        {
            System.out.println("##### listener : "+message);
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

            {{namePascalCase}} {{name}} = null;

            try{
                {{name}}=objectMapper.readValue( message,{{namePascalCase}}.class );
                System.out.println(" #### type = "+{{name}}.getEventType());

                if( {{name}}.getEventType()!=null &&
                    {{name}}.getEventType().equals( {{namePascalCase}}.class.getSimpleName()) )
                    {
                        // TO-DO :: Implement your Logic here.
                    }
                }catch(Exception e){

             }
        }
{{/events}}

}
