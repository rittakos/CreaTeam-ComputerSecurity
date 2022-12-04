import {Component, EventEmitter, Input, Output} from "@angular/core";
import {CaffResponse} from "../../../../app/api/webshop/models/caff-response";

@Component({
  selector: 'user-file-list',
  templateUrl: 'user-file-list.component.html',
  styleUrls: ['user-file-list.component.scss']
})
export class UserFileListComponent {
  @Input() files!: CaffResponse[];
  @Output() deleteFile = new EventEmitter<number>();

  onDeleteFileRequest(id: number): void {
    this.deleteFile.emit(id);
  }
}
