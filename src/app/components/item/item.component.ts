import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {IResponse} from "../../interfaces/response.interface";
import {ModalComponent} from "../modal/modal.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ItemComponent {

  @Input()
  item: IResponse;

  constructor(
    private modalWindow: MatDialog
  ) {
  }

  ngOnInit(): void {
  }

  get webSite() {
    return this.item.Cells.WebSite;
  }

  get emails() {
    return this.item.Cells.Email;
  }

  get commonName() {
    return this.item.Cells.CommonName;
  }

  openModal() {
    this.modalWindow.open(ModalComponent, {
      width: '600px',
      data: this.item.Cells
    });
  }
}
