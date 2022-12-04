import {Component, Input} from "@angular/core";
import {CaffResponse} from "../../../../app/api/webshop/models/caff-response";
import {ProductImageData} from "../../../../interfaces/product-image-data.interface";

@Component({
  selector: 'product-list',
  templateUrl: 'product-list.component.html',
  styleUrls: ['product-list.component.scss']
})
export class ProductListComponent{
  @Input() products!: CaffResponse[];
  @Input() images!: ProductImageData[];

  findImage(id: number): Blob {
    // if((this.images.find(image => image.id == id))) {
    //   return new Blob();
    // }
    return this.images.find(image => image.id == id)!.image;
  }
}
