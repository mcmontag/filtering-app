import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { SearchComponent } from './search/search.component';
import { CustomersComponent } from './customers/customers.component';
import { AppRoutingModule } from './app-routing.module';
import { SortComponent } from './sort/sort.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    CustomersComponent,
    SortComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
