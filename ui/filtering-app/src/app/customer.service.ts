import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from './customer';
import { environment } from 'src/environments/environment';
import { BackendPaths } from 'src/enums/paths';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private httpClient: HttpClient) { }

  searchCustomersByName(partialName: string): Observable<Customer[]> {
    if (partialName) {
      return this.httpClient.get<Customer[]>(`${environment.baseUri}${BackendPaths.SEARCH}/${partialName}?limit=${environment.defaultLimit}`);
    } else {
      return this.httpClient.get<Customer[]>(`${environment.baseUri}${BackendPaths.SEARCH}?limit=${environment.defaultLimit}`);
    }
  }

  getFirstNCustomersSortedBy(n: number, sortBy: 'firstName' | 'lastName' | 'companyName', sortDirection: 'asc' | 'desc'): Observable<Customer[]> {
    let limit = Math.max(environment.sortMin, Math.min(n, environment.sortMax));
    return this.httpClient.get<Customer[]>(`${environment.baseUri}${BackendPaths.SORT}?limit=${limit}&sortBy=${sortBy}&sortDirection=${sortDirection}`);
  }
}
