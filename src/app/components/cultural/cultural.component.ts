import {Component, OnInit} from '@angular/core';
import {ApiCulturalService} from "../../services/api-cultural.service";
import {EntityId} from "../../enums/entity-id";
import {IResponse} from "../../interfaces/response.interface";

@Component({
  selector: 'app-cultural',
  templateUrl: './cultural.component.html',
  styleUrls: ['./cultural.component.scss'],
})

export class CulturalComponent implements OnInit {
  search: string = "";
  loadingM: boolean = false;
  loadingT: boolean = false;

  theatres: IResponse[] = [];
  movieTheatres: IResponse[] = [];

  constructor(private theatreService: ApiCulturalService,
  ) {
  }

  trackByFn = (index: number, element: IResponse) => {
    return element.Cells.FullName;
  }

  ngOnInit(): void {
    this.loadMovieTheatres();
    this.loadTheatres()
  }

  loadMovieTheatres() {
    this.loadingM = true;
    this.theatreService.getInfo(EntityId.MOVIE_THEATER, this.movieTheatres?.length).subscribe(
      response => {
        this.movieTheatres = this.movieTheatres.concat(response);
        this.loadingM = false;
      })
  }

  loadTheatres() {
    this.loadingT = true;
    this.theatreService.getInfo(EntityId.THEATER, this.theatres?.length).subscribe(
      response => {
        this.theatres = this.theatres.concat(response);
        this.loadingT = false;
      })
  }
}
