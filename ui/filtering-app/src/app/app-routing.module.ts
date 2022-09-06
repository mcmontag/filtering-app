import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './search/search.component';
import { SortComponent } from './sort/sort.component';

const routes: Routes = [
  { path: `customers/search`, component: SearchComponent },
  { path: `customers/sort`, component: SortComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
