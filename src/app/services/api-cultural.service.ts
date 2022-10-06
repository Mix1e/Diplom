import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {IResponse} from "../interfaces/response.interface";
import {EntityId} from "../enums/entity-id";
import {environment} from "../../environments/environment";

const body = [
  "ChiefName",
  "ChiefPosition",
  "ClarificationOfWorkingHours",
  "CommonName",
  "Email",
  "FullName",
  "PublicPhone",
  "ShortName",
  "WebSite",
  "WorkingHours"
];

@Injectable({
  providedIn: 'root'
})
export class ApiCulturalService {
  static RESPONSE_COUNT = 20;

  constructor(private http: HttpClient) {
  }

  getInfo(entity: EntityId, skip: number): Observable<IResponse[]> {
    const params = new URLSearchParams();
    params.append("$top", ApiCulturalService.RESPONSE_COUNT.toString());
    params.append("$skip", skip.toString());
    params.append('api_key', environment.apiKey)
    return this.http.post<IResponse[]>(environment.apiTheatreUrl + `/${entity}` + "/rows" + `?${params}`, body, {
      headers: {
        'Access-Control-Allow-Methods': 'POST',
        'Access-Control-Allow-Headers': 'Content-Type, Authorization'}
    })
  }

}
