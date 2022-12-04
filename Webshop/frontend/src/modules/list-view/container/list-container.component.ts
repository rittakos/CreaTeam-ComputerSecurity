import {Component, OnInit} from "@angular/core";
import {CaffResponse} from "../../../app/api/webshop/models/caff-response";
import {FilesService} from "../../../app/api/webshop/services/files.service";
import {ProductImageData} from "../../../interfaces/product-image-data.interface";

@Component({
  selector: 'summary',
  templateUrl: 'list-container.component.html',
  styleUrls: ['list-container.component.scss']
})
export class ListContainerComponent implements OnInit {
  products: CaffResponse[] | undefined;
  images: ProductImageData[]= [];

  constructor(private readonly fileService: FilesService) { }

  ngOnInit(): void {
    this.fileService.getFilesSearch().subscribe({
      next: files => {
        this.products = files;
        this.loadImages();
      },
    });
  }

  submitSearch(name: string): void {
    this.fileService.getFilesSearch({query: name}).subscribe({
      next: resp => {
        this.products = resp;
        this.loadImages();
      }
    })
  }

  private loadImages(): void {
    this.products!.forEach(file => {
      this.fileService.getFilesPreview({id: file.id}).subscribe({
        next: resp => this.images.push({id: file.id, image: resp})
      })
    })
  }
}
