import {Component, EventEmitter, Input, Output} from "@angular/core";
import {FileData} from "../../../../interfaces/file-data.interface";

@Component({
  selector: 'user-file-list',
  templateUrl: 'user-file-list.component.html',
  styleUrls: ['user-file-list.component.scss']
})
export class UserFileListComponent {
  @Input() files!: FileData[];
  @Input() title: string | undefined;
  @Output() deleteFile = new EventEmitter<number>();

  onDeleteFileRequest(id: number): void {
    this.deleteFile.emit(id);
  }
}
