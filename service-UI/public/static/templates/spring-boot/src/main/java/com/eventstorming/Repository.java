forEach: Aggregate
fileName: {{namePascalCase}}Repository.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by uengine on 2019. 10. 24..
 */
public interface {{namePascalCase}}Repository extends PagingAndSortingRepository<{{namePascalCase}}, {{aggregateRoot.keyFieldDescriptor.className}}>{


}