/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpContext } from '@angular/common/http';
import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';
import { RequestBuilder } from '../request-builder';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { LoginRequest } from '../models/login-request';
import { LoginResponse } from '../models/login-response';
import { NewTokenResponse } from '../models/new-token-response';
import { RefreshTokenRequest } from '../models/refresh-token-request';
import { RegistrationRequest } from '../models/registration-request';

@Injectable({
  providedIn: 'root',
})
export class AuthService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation postAuthLogin
   */
  static readonly PostAuthLoginPath = '/auth/login';

  /**
   * login api
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postAuthLogin()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthLogin$Response(params?: {
    context?: HttpContext
    body?: LoginRequest
  }
): Observable<StrictHttpResponse<LoginResponse>> {

    const rb = new RequestBuilder(this.rootUrl, AuthService.PostAuthLoginPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<LoginResponse>;
      })
    );
  }

  /**
   * login api
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `postAuthLogin$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthLogin(params?: {
    context?: HttpContext
    body?: LoginRequest
  }
): Observable<LoginResponse> {

    return this.postAuthLogin$Response(params).pipe(
      map((r: StrictHttpResponse<LoginResponse>) => r.body as LoginResponse)
    );
  }

  /**
   * Path part for operation postAuthLoginRefresh
   */
  static readonly PostAuthLoginRefreshPath = '/auth/login/refresh';

  /**
   * refresh login api
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postAuthLoginRefresh()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthLoginRefresh$Response(params?: {
    context?: HttpContext
    body?: RefreshTokenRequest
  }
): Observable<StrictHttpResponse<NewTokenResponse>> {

    const rb = new RequestBuilder(this.rootUrl, AuthService.PostAuthLoginRefreshPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<NewTokenResponse>;
      })
    );
  }

  /**
   * refresh login api
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `postAuthLoginRefresh$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthLoginRefresh(params?: {
    context?: HttpContext
    body?: RefreshTokenRequest
  }
): Observable<NewTokenResponse> {

    return this.postAuthLoginRefresh$Response(params).pipe(
      map((r: StrictHttpResponse<NewTokenResponse>) => r.body as NewTokenResponse)
    );
  }

  /**
   * Path part for operation postAuthLogout
   */
  static readonly PostAuthLogoutPath = '/auth/logout';

  /**
   * logout api
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postAuthLogout()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthLogout$Response(params?: {
    context?: HttpContext
    body?: RefreshTokenRequest
  }
): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, AuthService.PostAuthLogoutPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'text',
      accept: '*/*',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
      })
    );
  }

  /**
   * logout api
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `postAuthLogout$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthLogout(params?: {
    context?: HttpContext
    body?: RefreshTokenRequest
  }
): Observable<void> {

    return this.postAuthLogout$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation postAuthRegister
   */
  static readonly PostAuthRegisterPath = '/auth/register';

  /**
   * registration api
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postAuthRegister()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthRegister$Response(params?: {
    context?: HttpContext
    body?: RegistrationRequest
  }
): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, AuthService.PostAuthRegisterPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'text',
      accept: '*/*',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
      })
    );
  }

  /**
   * registration api
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `postAuthRegister$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postAuthRegister(params?: {
    context?: HttpContext
    body?: RegistrationRequest
  }
): Observable<void> {

    return this.postAuthRegister$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

}
