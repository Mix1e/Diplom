import {Component, Input, OnInit} from '@angular/core';
import {IResponse} from "../../interfaces/response.interface";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent implements OnInit {

  @Input() item: IResponse;

  constructor() {
  }

  ngOnInit(): void {
  }

}
