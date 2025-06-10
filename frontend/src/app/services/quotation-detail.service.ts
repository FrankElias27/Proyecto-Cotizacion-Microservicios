import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../model/page';
import { QuotationDetail } from '../model/quotationDetail';

@Injectable({
  providedIn: 'root'
})
export class QuotationDetailService {

  private baseUrl = 'http://localhost:9000/api/quotation-detail';

  constructor(private http: HttpClient) { }

  getQuotationDetail(page: number = 0, size: number = 10, sort?: string): Observable<Page<QuotationDetail>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<QuotationDetail>>(`${this.baseUrl}/page`, { params });
  }

  getByQuotationId(quotationId: number,page: number = 0,size: number = 10,sort?: string): Observable<Page<QuotationDetail>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

      if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<QuotationDetail>>(
      `${this.baseUrl}/page/quotation/${quotationId}`,
      { params }
    );
  }

  getDetailsByQuotationId(quotationId: number): Observable<QuotationDetail[]> {
    const url = `${this.baseUrl}/${quotationId}`;
    return this.http.get<QuotationDetail[]>(url);
  }


  addQuotationDetail(quotationDetailRequest: QuotationDetail): Observable<void> {
    return this.http.post<void>(this.baseUrl, quotationDetailRequest);
  }

  deleteQuotationDetail(id: number): Observable<void> {
      return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

}
