forEach: Policy
fileName: {{relationEventInfo.namePascalCase}}.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
{{#relationEventInfo}}
package {{options.package}};


public class {{relationEventInfo.namePascalCase}} extends AbstractEvent {

{{#relationEventInfo.fieldDescriptors}}
    private {{className}} {{name}};
{{/relationEventInfo.fieldDescriptors}}

{{#relationEventInfo.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{name}};
    }

    public void set{{namePascalCase}}({{className}} {{name}}) {
        this.{{name}} = {{name}};
    }
{{/relationEventInfo.fieldDescriptors}}
}
{{/relationEventInfo}}
