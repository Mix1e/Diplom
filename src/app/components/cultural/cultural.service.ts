import {Injectable} from '@angular/core';
import {EntityId} from "../../enums/entity-id";
import {BehaviorSubject, finalize, Subscription, tap} from "rxjs";
import {ApiCulturalService} from "../../services/api-cultural.service";
import {IResponse} from "../../interfaces/response.interface";

@Injectable({
  providedIn: 'root'
})
export class CulturalService {

  private allMTLoaded$ = new BehaviorSubject<boolean>(false);
  private allTLoaded$ = new BehaviorSubject<boolean>(false);
  private errMes$ = new BehaviorSubject<string>("");

  private readonly movieTheatres$ = new BehaviorSubject<IResponse[]>([]);
  private readonly loadingMT$ = new BehaviorSubject<boolean>(true);
  private movieTheatresSubscription: Subscription;

  private readonly theatres$ = new BehaviorSubject<IResponse[]>([]);
  private readonly loadingT$ = new BehaviorSubject<boolean>(true);
  private theatresSubscription: Subscription;


  constructor(private apiCulturalService: ApiCulturalService
  ) {
  }

  get errorMessage$() {
    return this.errMes$.asObservable();
  }

  get getMovieTheatres$() {
    return this.movieTheatres$.asObservable();
  }

  get isLoadingMovieTheatres$() {
    return this.loadingMT$.asObservable();
  }

  get isAllMTLoaded$() {
    return this.allMTLoaded$.asObservable();
  }


  get getTheatres$() {
    return this.theatres$.asObservable();
  }

  get isLoadingTheatres$() {
    return this.loadingT$.asObservable();
  }

  get isAllTLoaded$() {
    return this.allTLoaded$.asObservable();
  }

  initData():void {
    this.loadMovieTheatres();
    //Сервер иногда выдаёт ошибку если сделать 2 запроса одновременно, да костыль, но работает
    setTimeout(() => this.loadTheatres(), 1);
  }

  private generateError(type: string): void {
    if (this.errMes$.value) {
      this.errMes$.next(this.errMes$.value + `и ${type}`);
    }
    else {
      this.errMes$.next(`Ошибка при загрузке списка ${type}`);
    }
  }

  loadMovieTheatres() {
    this.loadingMT$.next(true);
    this.movieTheatresSubscription = this.apiCulturalService.getInfo(EntityId.MOVIE_THEATER, this.movieTheatres$.value?.length || 0)
      .pipe(
        tap(response => {
          if (response.length % ApiCulturalService.RESPONSE_COUNT != 0) {
            this.allMTLoaded$.next(true);
          }
          this.movieTheatres$.next([...(this.movieTheatres$.value || []), ...response]);
        }),
        finalize(() => {
          this.loadingMT$.next(false)
        })
      )
      .subscribe(
        {
          error: () => {
            this.generateError('кинотеатров');
          }
        })
  }

  loadTheatres() {
    this.loadingT$.next(true);
    this.theatresSubscription = this.apiCulturalService.getInfo(EntityId.THEATER, this.theatres$.value?.length || 0)
      .pipe(
        tap(response => {
          if (response.length % ApiCulturalService.RESPONSE_COUNT != 0) {
            this.allTLoaded$.next(true);
          }
          this.theatres$.next([...(this.theatres$.value || []), ...response]);
        }),
        finalize(() => {
          this.loadingT$.next(false)
        })
      )
      .subscribe(
        {
          error: () => {
            this.generateError('театров');
          }
        })
  }


  unsubscribeAll():void {
    this.movieTheatresSubscription.unsubscribe();
    this.theatresSubscription.unsubscribe();
  }
}
