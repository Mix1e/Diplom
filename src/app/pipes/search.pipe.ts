import {Pipe, PipeTransform} from "@angular/core";
import {IResponse} from "../interfaces/response.interface";

@Pipe({
  name: 'searchPipe'
})
export class SearchPipe implements PipeTransform{
  transform(responses?: IResponse[], str?: string): IResponse[] {
    if (!responses) {
      return <IResponse[]>[];
    }
    if (!str) return responses;
    return responses.filter(e => {
      return e.Cells.ShortName.toLowerCase().includes(str.toLowerCase())
    });
  }
}
