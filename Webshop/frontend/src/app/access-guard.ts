import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router } from "@angular/router";
import { UserService } from "./services/user.service";

@Injectable()
export class AccessGuard implements CanActivate {
  private loggedIn: boolean = false;
  private admin: boolean = false;

  constructor(private userService: UserService, private router: Router) {
    this.userService.isLoggedIn().subscribe(loggedIn => this.loggedIn = loggedIn);
    this.userService.isAdmin().subscribe(admin => this.admin = admin);
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (route.data['requiresAdmin']) {
      if (!this.admin && route.data['redirectTo']) {
        this.router.navigate([route.data['redirectTo']]);
      }
      return this.admin;
    } else if (route.data['requiresLogin']) {
      if (!this.loggedIn && route.data['redirectTo']) {
        this.router.navigate([route.data['redirectTo']]);
      }
      return this.loggedIn;
    } else if (route.data['requiresLogout']) {
      if (this.loggedIn && route.data['redirectTo']) {
        this.router.navigate([route.data['redirectTo']]);
      }
      return !this.loggedIn;
    }
    return true;
  }
}
