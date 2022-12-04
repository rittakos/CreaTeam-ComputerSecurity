import {NgModule} from "@angular/core";
import {ProductListComponent} from "./components/product-list/product-list.component";
import {ProductCardComponent} from "./components/product-card/product-card.component";
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {SearchComponent} from "./components/search/search.component";
import {ListContainerComponent} from "./container/list-container.component";
import {ReactiveFormsModule} from "@angular/forms";

const routes: Routes = [
  {
    path: '',
    component: ListContainerComponent
  }
]

@NgModule({
  imports: [
    NgForOf,
    NgIf,
    RouterModule.forChild(routes),
    CurrencyPipe,
    DatePipe,
    ReactiveFormsModule,
  ],
  declarations: [
    ListContainerComponent,
    ProductListComponent,
    ProductCardComponent,
    SearchComponent,
  ],
  exports: []
})
export class ListViewModule {}
