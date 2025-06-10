import { Client } from "./client";
import { QuotationDetail } from "./quotationDetail";
import { StatusQuotation } from "./status-quotation";

export interface Quotation {
  id?: number;
  date: Date;
  client: Client;
  subject:string;
  status: StatusQuotation;
  total: number;
  details: QuotationDetail[];

}
