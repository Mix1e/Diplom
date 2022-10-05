import {Pipe, PipeTransform} from "@angular/core";
import {IEmail} from "../interfaces/email.interface";

@Pipe({
  name: 'email'
})
export class EmailPipe implements PipeTransform {
  transform(emails?: IEmail[]): string {
    if (!emails) {
      return "";
    }
    let str:string = "Email: \n";
    emails.forEach(x => {
      str += `${x.Email}\n`;
    })
    return str;
  }
}
