import { Client } from "./client";
import { clientDetails } from "./clientDetails";
import { QuotationDetail } from "./quotationDetail";
import { StatusQuotation } from "./status-quotation";

export interface Quotation {
  id?: number;
  date: Date;
  clientId: number;
  clientName:string;
  clientEmail:string;
  subject:string;
  status: StatusQuotation;
  total: number;
  clientDetails:clientDetails
  details: QuotationDetail[];

}
