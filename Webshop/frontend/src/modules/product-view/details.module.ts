import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ProductDetailsComponent} from "./components/details/product-details.component";
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {CommentsComponent} from "./components/comment/comments.component";
import {DetailsContainerComponent} from "./container/details-container.component";
import {SidebarComponent} from "./components/sidebar/sidebar.component";
import {ReactiveFormsModule} from "@angular/forms";

const routes: Routes = [
  {
    path: ':id',
    component: DetailsContainerComponent
  }
]

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    NgIf,
    NgForOf,
    CurrencyPipe,
    DatePipe,
    ReactiveFormsModule,
    ],
  declarations: [
    DetailsContainerComponent,
    ProductDetailsComponent,
    CommentsComponent,
    SidebarComponent,
  ],

})
export class DetailsModule {}
