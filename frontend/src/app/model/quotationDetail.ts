import { Quotation } from "./quotation";

export interface QuotationDetail {
   id?: number;
  quotation?: Quotation;
  productId: number;
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}
