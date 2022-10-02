
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
  Email: {
    Email: string;
  }[];
  FullName: string;
  PublicPhone: {
    PublicPhone: string;
  }[];
  ShortName: string;
  WebSite: string;
  WorkingHours: {
    DayWeek: string;
    WorkHours: string;
  }[];
}
