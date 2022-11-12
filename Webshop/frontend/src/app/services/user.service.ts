import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, ReplaySubject } from 'rxjs';
import { LoginResponse, NewTokenResponse, UserDetailsResponse } from '../api/webshop/models';
import { AuthService } from '../api/webshop/services';

const TOKEN_KEY = 'tokens';
const ADMIN = 'ROLE_ADMIN';

type TokenPayload = UserDetailsResponse & {
  exp: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private tokens?: NewTokenResponse;
  private loggedIn: ReplaySubject<boolean> = new ReplaySubject();
  private admin: ReplaySubject<boolean> = new ReplaySubject();

  constructor(
    private readonly router: Router,
    private readonly authService: AuthService,
  ) {
    this.loggedIn.next(false);
    const tokens = localStorage.getItem(TOKEN_KEY);

    if (tokens) {
      const parsedTokens = JSON.parse(tokens) as NewTokenResponse;

      if (!parsedTokens) {
        return;
      }

      if (!this.isExpired(parsedTokens.accessToken)) {
        this.setTokens(parsedTokens);
      } else if (!this.isExpired(parsedTokens.refreshToken)) {
        this.refresh(parsedTokens.refreshToken);
      } else {
        this.logout();
      }
    }
  }

  login(username: string, password: string): Observable<LoginResponse> {
    const response = this.authService.postAuthLogin({body: { username, password }});

    return new Observable<LoginResponse>(subscriber => {
      response.subscribe({
        next: res => {
          this.setTokens({ accessToken: res.accessToken, refreshToken: res.refreshToken });
          this.setRoles(res.accessToken);
          this.router.navigate(['/']);
          subscriber.next(res);
        },
        error: err => subscriber.error(err),
      });
    });
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  isAdmin(): Observable<boolean> {
    return this.admin.asObservable();
  }

  register(name: string, username: string, email: string, password: string, confirmPassword: string): Observable<void> {
    return this.authService.postAuthRegister({ body: { name, username, email, password, confirmPassword } });
  }

  getAccessToken(): string | undefined {
    return this.tokens?.accessToken;
  }

  logout(): void {
    const refreshToken = this.tokens?.refreshToken;
    this.loggedIn.next(false);
    localStorage.removeItem(TOKEN_KEY);
    this.tokens = undefined;
    this.router.navigate(['/auth']);

    if (!refreshToken) {
      return;
    }

    this.authService.postAuthLogout({ body: { refreshToken } }).subscribe();
  }

  private setTokens(tokens: NewTokenResponse): void {
    this.tokens = tokens;
    this.loggedIn.next(true);
    const expiration = this.getTokenPayload(tokens.accessToken).exp * 1000;
    localStorage.setItem(TOKEN_KEY, JSON.stringify(tokens));
    this.router.navigate([this.router.url]);
    if (tokens.refreshToken) {
      setTimeout(() => this.refresh(tokens.refreshToken), expiration - Date.now() - 10000);
    }
  }

  private refresh(refreshToken: string): void {
    this.authService.postAuthLoginRefresh({ body: { refreshToken } }).subscribe({
      next: res => {
        this.setTokens({ accessToken: res.accessToken, refreshToken: res.refreshToken });
        this.setRoles(res.accessToken);
      },
      error: err => {
        console.error(err);
        this.logout();
      }
    });
  }

  private getTokenPayload(token: string): TokenPayload {
    const payload = token.split('.')[1];
    const parsed = JSON.parse(window.atob(payload)) as TokenPayload;
    return parsed;
  }

  private isExpired(token: string): boolean {
    const payload = this.getTokenPayload(token);
    const expiration = payload.exp * 1000;
    return new Date(expiration) < new Date();
  }

  private setRoles(token: string) {
    const payload = this.getTokenPayload(token);
    const hasAdminRole = payload.roles.some(r => r === ADMIN);
    this.admin.next(hasAdminRole);
  }

}
