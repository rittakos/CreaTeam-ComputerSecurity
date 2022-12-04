import {Component, EventEmitter, Input, Output} from "@angular/core";
import {PublicUserDetailsResponse} from "../../../../app/api/webshop/models/public-user-details-response";

@Component({
  selector: 'sidebar',
  templateUrl: 'sidebar.component.html',
  styleUrls: ['sidebar.component.scss']
})
export class SidebarComponent {
  @Input() price!: number;
  @Input() creator!: PublicUserDetailsResponse;
  @Input() uploadDate!: string;
  @Input() purchased!: boolean;
  @Output() buyEvent = new EventEmitter();
  @Output() downloadEvent = new EventEmitter();

  onBuyClicked(): void {
    this.buyEvent.emit();
  }

  onDownloadClicked(): void {
    this.downloadEvent.emit();
  }
}
