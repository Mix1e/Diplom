import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClient, HttpHandler, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {IResponse} from "../interfaces/response.interface";
import {EntityId} from "../enums/entity-id";

const body = [
  "ChiefName",
  "ChiefPosition",
  "ChiefOrg",
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
export class CulturalService {

  private apiTheatreUrl = 'https://apidata.mos.ru/v1/datasets';
  private apiKey = 'f04fab203b351ba109b333521131772a';
  private static RESPONSE_COUNT = 20;

  constructor(private http: HttpClient) {
  }

  getTheatres(entity: EntityId, skip: number): Observable<IResponse[]> {
    const params = new URLSearchParams();
    params.append("$top", CulturalService.RESPONSE_COUNT.toString());
    params.append("$skip", skip.toString());
    params.append('api_key', this.apiKey)
    return this.http.post<IResponse[]>(this.apiTheatreUrl + `/${entity}` + "/rows" + `?${params}`, body)
  }
}
