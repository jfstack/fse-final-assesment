import { Pipe, PipeTransform } from '@angular/core';
import { ProjectDetails } from 'src/app/models/project-details';

@Pipe({
  name: 'filterProject'
})
export class FilterProjectPipe implements PipeTransform {

  transform(items: ProjectDetails[], searchTerm?: string): any {
    if(!items) return [];
    if(!searchTerm) return items;

    searchTerm = searchTerm.toLowerCase();

    return items.filter(it => {
      return it.project.toLowerCase().includes(searchTerm);
    });

  }

}
