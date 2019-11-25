forEach: Policy
fileName: {{relationEventInfo.namePascalCase}}.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
{{#relationEventInfo}}
package {{options.package}};

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{relationEventInfo.aggregateName}}table")
public class {{relationEventInfo.namePascalCase}} {

{{#relationEventInfo.fieldDescriptors}}
{{#isKey}}
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
{{/isKey}}
        private {{className}} {{name}};
{{/relationEventInfo.fieldDescriptors}}

{{#relationEventInfo.fieldDescriptors}}
        public {{className}} get{{namePascalCase}}() {
            return {{relationEventInfo.name}};
        }

        public void set{{relationEventInfo.namePascalCase}}({{className}} {{name}}) {
            this.{{name}} = {{name}};
        }
{{/relationEventInfo.fieldDescriptors}}
}
{{/relationEventInfo}}
