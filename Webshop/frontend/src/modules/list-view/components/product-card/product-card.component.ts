import {Component, Input, OnInit} from "@angular/core";
import {CaffResponse} from "../../../../app/api/webshop/models/caff-response";

@Component({
  selector: 'product-card',
  templateUrl: 'product-card.component.html',
  styleUrls: ['product-card.component.scss']
})
export class ProductCardComponent implements OnInit {
  @Input() product!: CaffResponse;
  @Input() image!: Blob;

  renderedImage: any;

  ngOnInit() {
    this.createImageFromBlob(this.image);
  }

  createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener("load", () => {
      this.renderedImage = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }
}
