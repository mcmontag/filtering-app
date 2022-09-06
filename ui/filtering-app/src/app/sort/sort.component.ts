import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute} from '@angular/router';
import { Observable} from 'rxjs';

import {
   debounceTime, distinctUntilChanged, map, switchMap
} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Customer } from '../customer';

import { CustomerService } from '../customer.service';

export interface SortOption {
  displayName: string,
  sortBy: 'firstName' | 'lastName' | 'companyName',
  sortDirection: 'asc' | 'desc'
}

@Component({
  selector: 'app-sort',
  templateUrl: './sort.component.html',
  styleUrls: [ './sort.component.scss' ]
})
export class SortComponent implements OnInit {
  customers$!: Observable<Customer[]>;
  sortOption!: SortOption;
  availableOptions!: SortOption[];

  constructor(private customerService: CustomerService, private route: ActivatedRoute, private router: Router) {}

  sort(sortOption: SortOption): void {
    console.log(sortOption);
    if (sortOption) {
      this.router.navigate(['customers/sort'], { queryParams: { sortBy: sortOption.sortBy, sortDirection: sortOption.sortDirection } });
    } else {
      this.router.navigate(['customers/sort']);
    }
  }

  ngOnInit(): void {
    this.availableOptions = [
      { displayName: 'First Name, Ascending', sortBy: 'firstName', sortDirection: 'asc'},
      { displayName: 'First Name, Descending', sortBy: 'firstName', sortDirection: 'desc'},
      { displayName: 'Last Name, Ascending', sortBy: 'lastName', sortDirection: 'asc'},
      { displayName: 'Last Name, Descending', sortBy: 'lastName', sortDirection: 'desc'},
      { displayName: 'Company Name, Ascending', sortBy: 'companyName', sortDirection: 'asc'},
      { displayName: 'Company Name, Descending', sortBy: 'companyName', sortDirection: 'desc'}
    ];

    this.sortOption = this.parseQueryParams(this.route.snapshot.queryParams);
    this.customers$ = this.route.queryParams.pipe(
      map((params: any) => this.parseQueryParams(params)),
      debounceTime(50),
      distinctUntilChanged(),
      switchMap(sortOpt  => {
        return !sortOpt ? this.customers$ : this.customerService.getFirstNCustomersSortedBy(environment.defaultLimit, sortOpt.sortBy, sortOpt.sortDirection);
      })
    );
  }

  parseQueryParams(params: any): SortOption {
    return this.availableOptions.find(o => o.sortBy === params['sortBy'] && o.sortDirection === params['sortDirection']) || this.availableOptions[0];
  }

  parseString(displayName: string): SortOption {
    return this.availableOptions.find(o => o.displayName === displayName) || this.availableOptions[0];
  }
}
