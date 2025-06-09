import { Client } from "./client";

export interface Quotation {
  id?: number;
  date: Date;
  client: Client;
  subject:string;
  total: number;

}
