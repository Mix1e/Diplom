import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {CulturalComponent} from "./components/cultural/cultural.component";
import { ItemComponent } from './components/item/item.component';
import {HttpClientModule} from "@angular/common/http";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import {SearchPipe} from "./pipes/search.pipe";
import { ModalComponent } from './components/modal/modal.component';
import {WorkingHoursPipe} from "./pipes/working-hours.pipe";
import {PhonePipe} from "./pipes/phone.pipe";
import {EmailPipe} from "./pipes/email.pipe";
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    CulturalComponent,
    ItemComponent,
    SearchPipe,
    ModalComponent,
    WorkingHoursPipe,
    PhonePipe,
    EmailPipe,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
