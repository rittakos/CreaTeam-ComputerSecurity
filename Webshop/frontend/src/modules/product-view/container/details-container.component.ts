import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {FilesService} from "../../../app/api/webshop/services/files.service";
import {CaffDetailsResponse} from "../../../app/api/webshop/models/caff-details-response";
import {CommentService} from "../../../app/api/webshop/services/comment.service";


@Component({
  selector: 'details-container',
  templateUrl: 'details-container.component.html',
  styleUrls: ['details-container.component.scss']
})
export class DetailsContainerComponent implements OnInit {
  product: CaffDetailsResponse | undefined;
  id!: number;
  image!: any;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly fileService: FilesService,
    private readonly commentService: CommentService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params["id"];
      // this.product = productDetails.find(p => p.id == id);
    })
    this.refreshProduct(this.id);
    this.fileService.getFilesPreview({id: this.id}).subscribe({
      next: resp => this.createImageFromBlob(resp)
    })
  }

  private createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener("load", () => {
      this.image = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }

  private refreshProduct(id: number): void {
    this.fileService.getFileDetails({id: id}).subscribe({
      next: resp => this.product = resp
    })
  }

  onBuyProduct() {
    this.fileService.postFilesBuyId({id: this.id}).subscribe({
      next: () => {
        this.refreshProduct(this.product?.id!);
      }
    });
  }

  onDownloadProduct(): void {
    this.fileService.getFilesDownload({id: this.id}).subscribe({
      next: resp => this.downloadFile(resp)
    })
  }

  private downloadFile(response: Blob): void {
    const url = window.URL.createObjectURL(response);
    window.open(url);
  }

  onCommentSubmit(text: string): void {
    this.commentService.postFilesCommentFileId({fileId: this.id, body: {comment: text}}).subscribe({
      next: () => {
        this.refreshProduct(this.id)
      }
    })
  }
}
