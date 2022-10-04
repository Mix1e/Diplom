import {Pipe, PipeTransform} from "@angular/core";
import {IWorkingHours} from "../interfaces/response.interface";

@Pipe({
  name: 'workingHours'
})
export class WorkingHoursPipe implements PipeTransform {
  transform(workingHours?: IWorkingHours[]): string {
    if (!workingHours) {
      return "";
    }
    let str:string = "Рабочее время\n";
    workingHours.forEach(x => {
      str += `${x.DayWeek}: ${x.WorkHours} \n`;
    })
    return str;
  }
}
