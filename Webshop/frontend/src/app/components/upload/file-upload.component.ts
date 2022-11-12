import { Component } from "@angular/core";

@Component({
    selector: 'file-upload',
    templateUrl: './file-upload.component.html',
    styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

    fileName: string = '';
    selectedFile: File | null = null;
    selectedFileName: string = '';
    price: number = 0;

    constructor () {}

    onUpload() {
        //calls the service
    }

    onFileSelected(event:any) {
        let uploadingFile = document.querySelector("#uploading-file");
        this.selectedFile = <File>event.target.files[0];
        this.selectedFileName = event.target.files[0].name;
        if (uploadingFile) {
            uploadingFile.innerHTML = this.selectedFileName;
        }

    }

}