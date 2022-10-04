
export interface IResponse {
  Cells: ICell;
  Number: number;
}

export interface ICell {
  ChiefName: string;
  ChiefOrg?: string;
  ChiefPosition: string;
  ClarificationOfWorkingHours: string;
  CommonName: string;
  Email: IEmail[];
  FullName: string;
  PublicPhone: IPublicPhone[];
  ShortName: string;
  WebSite: string;
  WorkingHours: IWorkingHours[];
}

export interface IEmail {
  Email: string;
}
export interface IPublicPhone {
  PublicPhone: string;
}

export interface IWorkingHours {
  DayWeek: string;
  WorkHours: string;
}
