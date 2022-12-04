import {Component, EventEmitter, Input, Output} from "@angular/core";
import {FileData} from "../../../../interfaces/file-data.interface";

@Component({
  selector: 'user-file',
  templateUrl: 'user-file.component.html',
  styleUrls: ['user-file.component.scss']
})
export class UserFileComponent {
  @Input() file!: FileData;
  @Output() deleteFile = new EventEmitter<number>();

  onDeleteFile(): void {
    this.deleteFile.emit(this.file.id);
  }
}
