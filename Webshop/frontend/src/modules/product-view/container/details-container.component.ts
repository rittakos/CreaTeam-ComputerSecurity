import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {FilesService} from "../../../app/api/webshop/services/files.service";
import {CaffDetailsResponse} from "../../../app/api/webshop/models/caff-details-response";
import {CommentService} from "../../../app/api/webshop/services/comment.service";
import { HttpHeaders } from "@angular/common/http";


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
    this.fileService.getFilesDownload$Response({id: this.id}).subscribe({
      next: resp => this.downloadFile(resp.body, resp.headers)
    })
  }

  private downloadFile(response: Blob, headers: HttpHeaders): void {
    let filename = (this.product?.name || 'unknown') + '.caff';
    const contentDisposition = headers.get('content-disposition');
    if (contentDisposition) {
      const search = 'filename="';
      filename = contentDisposition.substring(contentDisposition.indexOf(search) + search.length, contentDisposition.length - 1);
    }
    const url = window.URL.createObjectURL(response);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    console.log(a.download)
    a.click();
    window.URL.revokeObjectURL(url);
  }

  onCommentSubmit(text: string): void {
    this.commentService.postFilesCommentFileId({fileId: this.id, body: {comment: text}}).subscribe({
      next: () => {
        this.refreshProduct(this.id)
      }
    })
  }
}
