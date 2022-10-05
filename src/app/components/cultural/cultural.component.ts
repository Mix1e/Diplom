import {Component, OnInit} from '@angular/core';
import {ApiCulturalService} from "../../services/api-cultural.service";
import {EntityId} from "../../enums/entity-id";
import {IResponse} from "../../interfaces/response.interface";
import {animate, state, style, transition, trigger} from "@angular/animations";


@Component({
  selector: 'app-cultural',
  templateUrl: './cultural.component.html',
  styleUrls: ['./cultural.component.scss'],animations: [
    trigger('instance', [
      state('changed', style({background: 'blue'} )),
      transition(':enter', [
        style({opacity: 0, transform: 'scale(0.7)'}),
        animate('450ms')
      ]),
      transition(':leave', [
        style({opacity: 1}),
        animate('400ms', style({
          opacity: 0,
          transform: 'scale(0.7)'
        }))
      ])
    ])
  ]
})
export class CulturalComponent implements OnInit {
  search = "";
  loadingM = false;
  loadingT = false;
  allMTLoaded = false;
  allTLoaded = false;

  theatres: IResponse[] = [];
  movieTheatres: IResponse[] = [];

  constructor(private theatreService: ApiCulturalService,
  ) {
  }

  ngOnInit(): void {
    this.loadMovieTheatres();
    this.loadTheatres();
  }

  trackByFn = (index: number, element: IResponse) => {
    return element.Cells.CommonName;
  }

  loadMovieTheatres() {
    this.loadingM = true;
    this.theatreService.getInfo(EntityId.MOVIE_THEATER, this.movieTheatres?.length).subscribe(
      response => {
        let curCount = this.movieTheatres.length;
        this.movieTheatres = this.movieTheatres.concat(response);
        this.loadingM = false;
        if (this.movieTheatres.length % ApiCulturalService.RESPONSE_COUNT != 0 || curCount == this.movieTheatres.length) {
          this.allMTLoaded = true;
        }
      })
  }

  loadTheatres() {
    this.loadingT = true;
    this.theatreService.getInfo(EntityId.THEATER, this.theatres?.length).subscribe(
      response => {
        let curCount = this.theatres.length;
        this.theatres = this.theatres.concat(response);
        this.loadingT = false;
        if (this.theatres.length % ApiCulturalService.RESPONSE_COUNT != 0 || curCount == this.theatres.length) {
          this.allTLoaded = true;
        }
      })
  }
}
