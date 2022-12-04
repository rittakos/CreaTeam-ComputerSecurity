import {Component, Input} from "@angular/core";
import {UserData} from "../../../../interfaces/user-data.interface";

@Component({
  selector: 'user-data',
  templateUrl: 'user-data.component.html',
  styleUrls: ['user-data.component.scss']
})
export class UserDataComponent {
  @Input() user!: UserData;
}
