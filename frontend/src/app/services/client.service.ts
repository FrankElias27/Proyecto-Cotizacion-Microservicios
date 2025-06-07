import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../model/client';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseUrl = '/api/client';

  constructor(private http: HttpClient) { }

  getClients(page: number = 0, size: number = 10, sort?: string): Observable<Page<Client>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<Client>>(this.baseUrl, { params });
  }
}
