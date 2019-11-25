forEach: Event
fileName: {{namePascalCase}}.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{aggregateName}}table")
public class {{namePascalCase}} extends AbstractEvent {

    {{#fieldDescriptors}}
    {{#isKey}}
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    {{/isKey}}
    private {{className}} {{name}};
    {{/fieldDescriptors}}

    {{#fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{name}};
     }

    public void set{{namePascalCase}}({{className}} {{name}}) {
        this.{{name}} = {{name}};
    }
    {{/fieldDescriptors}}
}
