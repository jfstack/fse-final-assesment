import { Pipe, PipeTransform } from '@angular/core';
import { TaskDetails } from 'src/app/models/task-details';

@Pipe({
  name: 'countTask'
})
export class CountTaskPipe implements PipeTransform {

  transform(items: TaskDetails[], param?: string): any {
    if(!items) return 0;

    if(!param) return items.length;

    return items.filter(it => { return it.status === param } ).length;
  }

}
