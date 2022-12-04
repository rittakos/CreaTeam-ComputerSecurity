import {Component, OnInit} from "@angular/core";
import {UserService} from "../../../app/services/user.service";
import {FilesService} from "../../../app/api/webshop/services/files.service";
import {UserData} from "../../../interfaces/user-data.interface";
import {FileData} from "../../../interfaces/file-data.interface";

@Component({
  selector: 'profile-container',
  templateUrl: 'profile-container.component.html',
  styleUrls: ['profile-container.component.scss']
})
export class ProfileContainerComponent implements OnInit {
  userData: UserData | undefined;
  userFiles: FileData[] = [];
  otherFiles: FileData[] = [];
  isAdmin: boolean = false;

  constructor(
    private readonly userService: UserService,
    private readonly fileService: FilesService,
  ) {}

  ngOnInit(): void {
    this.userService.getUserDetails().subscribe({
      next: resp => this.userData = resp
    });
    this.userService.isAdmin().subscribe({
      next: resp => this.isAdmin = resp
    });
    this.loadUserFiles();
  }

  private loadUserFiles(): void {
    this.userFiles = [];
    this.otherFiles = [];
    this.fileService.getFilesSearch().subscribe({
      next: resp => {
        resp.forEach(file => {
          this.fileService.getFileDetails({id: file.id}).subscribe({
            next: fileDetails => {
              if (fileDetails.creator.username == this.userData?.username) {
                this.userFiles?.push(file);
              } else if(this.isAdmin) {
                this.otherFiles.push({
                  ...file,
                  creator: fileDetails.creator.username
                });
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
