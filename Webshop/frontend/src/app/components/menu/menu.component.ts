import {Component, OnInit} from "@angular/core";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-menu',
  templateUrl: 'menu.component.html',
  styleUrls: ['menu.component.scss']
})
export class MenuComponent implements OnInit {
  loggedIn = false;

  constructor(private readonly userService: UserService) { }

  ngOnInit() {
    this.userService.isLoggedIn().subscribe({
      next: resp => this.loggedIn = resp
    });
  }

  onLogout(): void {
    this.userService.logout();
  }
}
