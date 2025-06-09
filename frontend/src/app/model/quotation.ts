import { Client } from "./client";
import { QuotationDetail } from "./quotationDetail";

export interface Quotation {
  id?: number;
  date: Date;
  client: Client;
  subject:string;
  status: string;
  total: number;
  details: QuotationDetail[];

}
