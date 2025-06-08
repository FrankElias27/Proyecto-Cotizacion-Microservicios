import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../model/client';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseUrl = 'http://localhost:9000/api/client';

  constructor(private http: HttpClient) { }

  getClients(page: number = 0, size: number = 10, sort?: string): Observable<Page<Client>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<Client>>(`${this.baseUrl}/page`, { params });
  }

  searchClients(query: string, page: number = 0, size: number = 10, sort?: string): Observable<Page<Client>> {
    let params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<Client>>(`${this.baseUrl}/search`, { params });
  }


  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.baseUrl}/${id}`);
  }


  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }


  updateClient(id: number, clientRequest: Client): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}`, clientRequest);
  }

  addClient(clientRequest: Client): Observable<void> {
  return this.http.post<void>(this.baseUrl, clientRequest);
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

  importClientsFromExcel(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.baseUrl}/import`, formData, {
      responseType: 'text'
    });
  }


}
