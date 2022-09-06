import { Component, OnInit } from '@angular/core';
import { Customer } from '../customer';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  customers: Customer[] | undefined;

  constructor(private customerService : CustomerService) { }

  ngOnInit(): void {
    this.customerService.searchCustomersByName("ick").subscribe((data: Customer[]) => {
      console.log(data);
      this.customers = data;
    });
  }
}
