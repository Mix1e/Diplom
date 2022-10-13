import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {ICell} from "../../interfaces/cell.interface";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";


@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ModalComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ICell) {}

  get fullName() {
    return this.data.FullName;
  }

  get chiefPosition() {
    return this.data.ChiefPosition;
  }

  get chiefName() {
    return this.data.ChiefName;
  }

  get emails() {
    return this.data.Email;
  }

  get publicPhones() {
    return this.data.PublicPhone;
  }

  get webSite() {
    return this.data.WebSite;
  }

  get workingHours() {
    return this.data.WorkingHours;
  }
}
