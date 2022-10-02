import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CulturalService} from "../../services/cultural.service";
import {EntityId} from "../../enums/entity-id";
import {IResponse} from "../../interfaces/response.interface";
import {finalize, map, Observable, tap} from "rxjs";

@Component({
  selector: 'app-theatre',
  templateUrl: './theatre.component.html',
  styleUrls: ['./theatre.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TheatreComponent implements OnInit {
  search:string = "";
  loading:boolean = false;

  currentResponses: IResponse[] = [];
  theatres$ = this.getTheatres$();
  movieTheatres: IResponse[] = [];

  constructor(private theatreService: CulturalService,
  ) {
  }

  trackByFn = (index: number, element: IResponse ) => {
    return element.Cells.FullName;
  }

  ngOnInit(): void {

    // this.getTheatres()
  }

  getMovieTheatres() {
    this.theatreService.getTheatres(EntityId.MOVIE_THEATER, this.movieTheatres?.length).subscribe(
      response => {
        this.movieTheatres = response;
    })
  }

  loadNext() {
    this.theatres$ = this.getTheatres$();
  }

  getTheatres$(): Observable<IResponse[]> {
    this.loading = true;
    return this.theatreService.getTheatres(EntityId.THEATER, this.currentResponses.length)
      .pipe(
        map(responses => {
          this.currentResponses = [...this.currentResponses, ...responses];
          return  this.currentResponses;
        }),
        finalize(() => {
          this.loading = false;
        })
      )
  }
}
