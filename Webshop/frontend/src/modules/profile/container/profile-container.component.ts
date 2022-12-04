import {Component, OnInit} from "@angular/core";
import {UserService} from "../../../app/services/user.service";
import {FilesService} from "../../../app/api/webshop/services/files.service";
import {CaffResponse} from "../../../app/api/webshop/models/caff-response";
import {UserData} from "../../../interfaces/user-data.interface";

@Component({
  selector: 'profile-container',
  templateUrl: 'profile-container.component.html',
  styleUrls: ['profile-container.component.scss']
})
export class ProfileContainerComponent implements OnInit {
  userData: UserData | undefined;
  userFiles: CaffResponse[] = [];

  constructor(
    private readonly userService: UserService,
    private readonly fileService: FilesService,
  ) {}

  ngOnInit(): void {
    this.userService.getUserDetails().subscribe({
      next: resp => this.userData = resp
    });
    this.loadUserFiles();
  }

  private loadUserFiles(): void {
    this.userFiles = [];
    this.fileService.getFilesSearch().subscribe({
      next: resp => {
        resp.forEach(file => {
          this.fileService.getFileDetails({id: file.id}).subscribe({
            next: fileDetails => {
              if(fileDetails.creator.username == this.userData?.username) {
                this.userFiles?.push(file);
              }
            }
          })
        })
      }
    });
  }

  onFileDeleteSubmit(id: number): void {
    this.fileService.deleteFilesDelete({id}).subscribe({
      next: () => this.loadUserFiles()
    });
  }

}
