import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterUser'
})
export class FilterUserPipe implements PipeTransform {

  transform(items: any[], searchTerm?: string): any[] {

    if(!items) return [];
    if(!searchTerm) return items;

    searchTerm = searchTerm.toLowerCase();

    return items.filter(it => {
      return it.firstName.toLowerCase().includes(searchTerm);
    });

  }

}
