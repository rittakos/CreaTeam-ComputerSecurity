import {Component, Input} from "@angular/core";
import {CaffDetailsResponse} from "../../../../app/api/webshop/models/caff-details-response";

@Component({
  selector: 'details-view',
  templateUrl: 'product-details.component.html',
  styleUrls: ['product-details.component.scss']
})
export class ProductDetailsComponent {
  @Input() product!: CaffDetailsResponse;
  @Input() image!: Blob;
}
