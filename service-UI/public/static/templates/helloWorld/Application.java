forEach: BoundedContext
---
public class Application{
    public static void main(String[]){
        System.out.println("BounedContext: {{name}}");

        {{#aggregates}}
        System.out.println("Aggregate: {{name}}");
        {{/aggregates}}
    }
}