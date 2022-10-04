import {Pipe, PipeTransform} from "@angular/core";
import {IPublicPhone} from "../interfaces/response.interface";

@Pipe({
  name: 'phoneNumber'
})
export class PhonePipe implements PipeTransform {
  transform(phones?: IPublicPhone[]): string {
    if (!phones) {
      return "";
    }
    let str:string = "Контактные номера: \n";
    phones.forEach(x => {
      str += `${x.PublicPhone}, `;
    })
    return str.replace(/,\s*$/, "");
  }
}
