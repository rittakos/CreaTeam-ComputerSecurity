import { Component } from "@angular/core";
import { FileUploadRequest } from "src/app/api/webshop/models";
import { FilesService } from "src/app/api/webshop/services";

@Component({
    selector: 'file-upload',
    templateUrl: './file-upload.component.html',
    styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

    fileName: string = '';
    selectedFile: File | null | Blob = null;
    selectedFileName: string = '';
    price: number = 0;

    isDisabled: boolean = true;
    minNameLength: number = 4;

    constructor (private readonly fileService: FilesService) {}

    onUpload() {
        if (this.selectedFile instanceof Blob) {
            const fileUploading: FileUploadRequest  = {
                file: this.selectedFile,
                name: this.fileName,
                price: 0
            }
            this.fileService.postFilesUpload({body: fileUploading}).subscribe({
                next: () => {
                    this.fileName = "";
                    this.selectedFileName = "";
                    this.selectedFile = null;
                  window.alert('Successful uploading');
                },
                error: err => window.alert(err.error),
            })

        }

    }

    onFileSelected(event:any) {
        let uploadingFile = document.querySelector("#uploading-file");
        this.selectedFile = <File>event.target.files[0];
        this.selectedFileName = event.target.files[0].name

        let badFileLabel = document.querySelector("#bad-file-label");
        
        if (uploadingFile) {
            uploadingFile.innerHTML = this.selectedFileName;
        }

        if (this.selectedFileName.endsWith(".caff")) {
            this.isDisabled = false;
            badFileLabel?.setAttribute("hidden", "hidden");
        }
        else {
            badFileLabel?.removeAttribute("hidden");
        }

        console.log(this.selectedFileName.endsWith(".caff") );


    }

}