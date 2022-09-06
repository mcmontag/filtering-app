import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute} from '@angular/router';
import { Observable} from 'rxjs';

import {
   debounceTime, distinctUntilChanged, map, switchMap
} from 'rxjs/operators';

import { Customer } from '../customer';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: [ './search.component.scss' ]
})
export class SearchComponent implements OnInit {
  customers$!: Observable<Customer[]>;
  term!: string;

  constructor(private customerService: CustomerService, private route: ActivatedRoute, private router: Router) {}

  search(term: string): void {
    if (term) {
      this.router.navigate(['customers/search'], { queryParams: { q: term } });
    } else {
      this.router.navigate(['customers/search']);
    }
  }

  ngOnInit(): void {
    this.term = this.route.snapshot.queryParams['q'];
    this.customers$ = this.route.queryParams.pipe(
      map(params => params['q'] || ''),
      debounceTime(50),
      distinctUntilChanged(),
      switchMap((term: string) => {
        return this.customerService.searchCustomersByName(term)
      })
    );
  }
}
