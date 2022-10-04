import {Component, Input, OnInit} from '@angular/core';
import {ICell} from "../../interfaces/response.interface";

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {

  @Input() cell:ICell;
  constructor() { }

  ngOnInit(): void {
  }

}
