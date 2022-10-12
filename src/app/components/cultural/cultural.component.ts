import {Component, Injectable, OnInit} from '@angular/core';
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

@Injectable()
export class CulturalComponent implements OnInit {
  search: string = "";
  loadingM: boolean = false;
  loadingT: boolean = false;
  allMTLoaded: boolean = false;
  allTLoaded: boolean = false;
  errMes: string = "";

  theatres: IResponse[] = [];
  movieTheatres: IResponse[] = [];

  constructor(private culturalService: ApiCulturalService,
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
    this.culturalService.getInfo(EntityId.MOVIE_THEATER, this.movieTheatres?.length).subscribe(
      response => {
        let curCount = this.movieTheatres.length;
        this.movieTheatres = this.movieTheatres.concat(response);
        this.loadingM = false;
        if (this.movieTheatres.length % ApiCulturalService.RESPONSE_COUNT != 0 || curCount == this.movieTheatres.length) {
          this.allMTLoaded = true;
        }
      },
      () => {
        this.errMes = `Ошибка при загрузке списка кинотеатров`;
      })
  }

  loadTheatres() {
    this.loadingT = true;
    this.culturalService.getInfo(EntityId.THEATER, this.theatres?.length).subscribe(
      response => {
        let curCount = this.theatres.length;
        this.theatres = this.theatres.concat(response);
        this.loadingT = false;
        if (this.theatres.length % ApiCulturalService.RESPONSE_COUNT != 0 || curCount == this.theatres.length) {
          this.allTLoaded = true;
        }
      },
      () => {
        this.errMes = `Ошибка при загрузке списка театров`;
      })
  }
}
