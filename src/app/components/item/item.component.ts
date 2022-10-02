import {Component, Input, OnInit} from '@angular/core';
import {IResponse} from "../../interfaces/response.interface";
import {CulturalService} from "../../services/cultural.service";
import {EntityId} from "../../enums/entity-id";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent implements OnInit {

  @Input() item: IResponse;

  constructor(
    private service:CulturalService
  ) { }

  ngOnInit(): void {
  }

}
