import {Component, Input} from '@angular/core';
import {IResponse} from "../../interfaces/response.interface";
import {ModalComponent} from "../modal/modal.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss'],
})
export class ItemComponent {

  @Input() item: IResponse;


  constructor(
    private modalWindow: MatDialog
  ) {
  }

  ngOnInit(): void {
  }

  openModal() {
    this.modalWindow.open(ModalComponent, {
      width: '600px',
      data: this.item.Cells
    });
  }
}
