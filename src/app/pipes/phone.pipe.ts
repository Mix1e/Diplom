import {Pipe, PipeTransform} from "@angular/core";
import {IPublicPhone} from "../interfaces/public-phone";

@Pipe({
  name: 'phoneNumber'
})
export class PhonePipe implements PipeTransform {
  transform(phones?: IPublicPhone[] | IPublicPhone): string {
    let str: string = "Контактные номера: \n";
    if (Array.isArray(phones)) {
      phones?.forEach(x => {
        str += `${x.PublicPhone}, `;
      })
    }
    else {
      str += phones.PublicPhone;
    }
    return str.replace(/,\s*$/, "");
  }
}
