import { ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';

export const startDateEndDateValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const startDate = control.get('startDate');
    const endDate = control.get('endDate');
  
    return startDate && endDate 
                && startDate.value > endDate.value ? { 'invalidStartEndDate': true } : null;
  };