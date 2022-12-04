import {Component, EventEmitter, Input, Output} from "@angular/core";
import {CaffResponse} from "../../../../app/api/webshop/models/caff-response";

@Component({
  selector: 'user-file',
  templateUrl: 'user-file.component.html',
  styleUrls: ['user-file.component.scss']
})
export class UserFileComponent {
  @Input() file!: CaffResponse;
  @Output() deleteFile = new EventEmitter<number>();

  onDeleteFile(): void {
    this.deleteFile.emit(this.file.id);
  }
}
