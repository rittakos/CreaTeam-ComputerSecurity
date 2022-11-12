import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router } from "@angular/router";
import { UserService } from "./services/user.service";

@Injectable()
export class AccessGuard implements CanActivate {
  private loggedIn: boolean = false;

  constructor(private userService: UserService, private router: Router) {
    this.userService.isLoggedIn().subscribe(loggedIn => this.loggedIn = loggedIn);
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (route.data['requiresLogin']) {
      if (!this.loggedIn && route.data['redirectTo']) {
        this.router.navigate([route.data['redirectTo']]);
      }
      return this.loggedIn;
    }
    if (route.data['requiresLogout']) {
      if (this.loggedIn && route.data['redirectTo']) {
        this.router.navigate([route.data['redirectTo']]);
      }
      return !this.loggedIn;
    }
    return true;
  }
}
