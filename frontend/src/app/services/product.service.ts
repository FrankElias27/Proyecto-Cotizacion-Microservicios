import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../model/product';
import { Page } from '../model/page';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:9000/api/product';

    constructor(private http: HttpClient) { }

    getProducts(page: number = 0, size: number = 10, sort?: string): Observable<Page<Product>> {
      let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());

      if (sort) {
        params = params.set('sort', sort);
      }

      return this.http.get<Page<Product>>(`${this.baseUrl}/page`, { params });
    }

    searchProducts(query: string, page: number = 0, size: number = 10, sort?: string): Observable<Page<Product>> {
      let params = new HttpParams()
        .set('query', query)
        .set('page', page.toString())
        .set('size', size.toString());

      if (sort) {
        params = params.set('sort', sort);
      }

      return this.http.get<Page<Product>>(`${this.baseUrl}/search`, { params });
    }


    getProductById(id: number): Observable<Product> {
      return this.http.get<Product>(`${this.baseUrl}/${id}`);
    }


    deleteProduct(id: number): Observable<void> {
      return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }


    updateProduct(id: number, productRequest: Product): Observable<void> {
      return this.http.put<void>(`${this.baseUrl}/${id}`, productRequest);
    }

    addProduct(productRequest: Product): Observable<void> {
    return this.http.post<void>(this.baseUrl, productRequest);
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

    importProductsFromExcel(file: File): Observable<string> {
      const formData = new FormData();
      formData.append('file', file);

      return this.http.post(`${this.baseUrl}/import`, formData, {
        responseType: 'text'
      });
    }

    getAllProducts(): Observable<Product[]> {
      return this.http.get<Product[]>(this.baseUrl);
    }
}
