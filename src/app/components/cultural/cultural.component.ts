import {ChangeDetectionStrategy, Component, Injectable, OnDestroy, OnInit} from '@angular/core';
import {IResponse} from "../../interfaces/response.interface";
import {instanceAnimation} from "../../animations/basic.animation";
import {CulturalService} from "./cultural.service";


@Component({
  selector: 'app-cultural',
  templateUrl: './cultural.component.html',
  styleUrls: ['./cultural.component.scss'],
  animations: [instanceAnimation],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

@Injectable()
export class CulturalComponent implements OnInit, OnDestroy {
  search: string = "";

  constructor(private culturalService: CulturalService,
  ) {}

  ngOnInit(): void {
    this.culturalService.initData();
  }

  trackByFn = (index: number, element: IResponse) => {
    return element.Cells.CommonName;
  }

  get errorMessage() {
    return this.culturalService.errorMessage;
  }


  get movieTheatres$() {
    return this.culturalService.getMovieTheatres$;
  }

  get allMTLoaded() {
    return this.culturalService.isAllMTLoaded;
  }

  get loadingMT$() {
    return this.culturalService.isLoadingMovieTheatres$;
  }

  loadMovieTheatres(): void {
    this.culturalService.loadMovieTheatres();
  }


  get theatres$() {
    return this.culturalService.getTheatres$;
  }

  get loadingT$() {
    return this.culturalService.isLoadingTheatres$;
  }

  get allTLoaded() {
    return this.culturalService.isAllTLoaded;
  }

  loadTheatres(): void {
    this.culturalService.loadTheatres();
  }


  ngOnDestroy(): void {
    this.culturalService.unsubscribeAll();
  }
}
