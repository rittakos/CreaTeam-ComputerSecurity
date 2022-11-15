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

import { ModifyUserRequest } from '../models/modify-user-request';
import { UserDetailsResponse } from '../models/user-details-response';

@Injectable({
  providedIn: 'root',
})
export class AdminService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation putAdminModifyUser
   */
  static readonly PutAdminModifyUserPath = '/admin/modifyUser/{id}';

  /**
   * modify details of a user
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `putAdminModifyUser()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  putAdminModifyUser$Response(params: {
    id: number;
    context?: HttpContext
    body?: ModifyUserRequest
  }
): Observable<StrictHttpResponse<UserDetailsResponse>> {

    const rb = new RequestBuilder(this.rootUrl, AdminService.PutAdminModifyUserPath, 'put');
    if (params) {
      rb.path('id', params.id, {});
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<UserDetailsResponse>;
      })
    );
  }

  /**
   * modify details of a user
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `putAdminModifyUser$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  putAdminModifyUser(params: {
    id: number;
    context?: HttpContext
    body?: ModifyUserRequest
  }
): Observable<UserDetailsResponse> {

    return this.putAdminModifyUser$Response(params).pipe(
      map((r: StrictHttpResponse<UserDetailsResponse>) => r.body as UserDetailsResponse)
    );
  }

  /**
   * Path part for operation getAdminUsers
   */
  static readonly GetAdminUsersPath = '/admin/users';

  /**
   * Your GET endpoint.
   *
   * list all users for the admin
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAdminUsers()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAdminUsers$Response(params?: {
    context?: HttpContext
  }
): Observable<StrictHttpResponse<Array<UserDetailsResponse>>> {

    const rb = new RequestBuilder(this.rootUrl, AdminService.GetAdminUsersPath, 'get');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Array<UserDetailsResponse>>;
      })
    );
  }

  /**
   * Your GET endpoint.
   *
   * list all users for the admin
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAdminUsers$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAdminUsers(params?: {
    context?: HttpContext
  }
): Observable<Array<UserDetailsResponse>> {

    return this.getAdminUsers$Response(params).pipe(
      map((r: StrictHttpResponse<Array<UserDetailsResponse>>) => r.body as Array<UserDetailsResponse>)
    );
  }

}
