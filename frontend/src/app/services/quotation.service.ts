import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../model/page';
import { Quotation } from '../model/quotation';

@Injectable({
  providedIn: 'root'
})
export class QuotationService {

  private baseUrl = 'http://localhost:9000/api/quotation';

  constructor(private http: HttpClient) { }

  getQuotations(page: number = 0, size: number = 10, sort?: string): Observable<Page<Quotation>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<Quotation>>(`${this.baseUrl}/page`, { params });
  }

  addQuotation(quotationRequest: Quotation): Observable<void> {
    return this.http.post<void>(this.baseUrl, quotationRequest);
  }

  getQuotationById(id: number): Observable<Quotation> {
    return this.http.get<Quotation>(`${this.baseUrl}/${id}`);
  }

  updateQuotation(id: number, quotationRequest: Quotation): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}`, quotationRequest);
  }

  deleteQuotation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  downloadReport(params: { [key: string]: any }): Observable<Blob> {
    let httpParams = new HttpParams();
    Object.keys(params).forEach(key => {
      httpParams = httpParams.set(key, params[key]);
    });

    return this.http.get(`${this.baseUrl}/report`, {
      params: httpParams,
      responseType: 'blob',
      observe: 'body'
    });
  }

}
