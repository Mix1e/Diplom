import {IEmail} from "./email.interface";
import {IPublicPhone} from "./public-phone";
import {IWorkingHours} from "./working-hours.interface";

export interface ICell {
  ChiefName: string;
  ChiefPosition: string;
  ClarificationOfWorkingHours: string;
  CommonName: string;
  Email: IEmail[];
  FullName: string;
  // @ts-ignore
  PublicPhone: IPublicPhone[];
  ShortName: string;
  WebSite: string;
  WorkingHours: IWorkingHours[];
}
