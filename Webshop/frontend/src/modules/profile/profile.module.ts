import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ProfileContainerComponent} from "./container/profile-container.component";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {UserDataComponent} from "./components/user-data/user-data.component";
import {UserFileListComponent} from "./components/user-file-list/user-file-list.component";
import {UserFileComponent} from "./components/user-file/user-file.component";

const routes: Routes = [
  {
    path: '',
    component: ProfileContainerComponent
  }
]

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        NgIf,
        NgForOf,
        DatePipe,
    ],
  declarations: [
    ProfileContainerComponent,
    UserDataComponent,
    UserFileListComponent,
    UserFileComponent
  ]
})
export class ProfileModule {}
