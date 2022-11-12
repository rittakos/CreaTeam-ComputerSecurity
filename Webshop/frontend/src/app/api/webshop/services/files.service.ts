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

import { CaffDetailsResponse } from '../models/caff-details-response';
import { CaffResponse } from '../models/caff-response';
import { CommentRequest } from '../models/comment-request';
import { CommentResponse } from '../models/comment-response';
import { ModifyCaffRequest } from '../models/modify-caff-request';

@Injectable({
  providedIn: 'root',
})
export class FilesService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation getFilesSearch
   */
  static readonly GetFilesSearchPath = '/files/search';

  /**
   * Your GET endpoint.
   *
   * search for caff files
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getFilesSearch()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFilesSearch$Response(params?: {
    query?: string;
    context?: HttpContext
  }
): Observable<StrictHttpResponse<Array<CaffResponse>>> {

    const rb = new RequestBuilder(this.rootUrl, FilesService.GetFilesSearchPath, 'get');
    if (params) {
      rb.query('query', params.query, {});
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Array<CaffResponse>>;
      })
    );
  }

  /**
   * Your GET endpoint.
   *
   * search for caff files
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getFilesSearch$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFilesSearch(params?: {
    query?: string;
    context?: HttpContext
  }
): Observable<Array<CaffResponse>> {

    return this.getFilesSearch$Response(params).pipe(
      map((r: StrictHttpResponse<Array<CaffResponse>>) => r.body as Array<CaffResponse>)
    );
  }

  /**
   * Path part for operation deleteFilesDelete
   */
  static readonly DeleteFilesDeletePath = '/files/delete/{id}';

  /**
   * delete an uploaded caff file
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteFilesDelete()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteFilesDelete$Response(params: {
    id: number;
    context?: HttpContext
  }
): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, FilesService.DeleteFilesDeletePath, 'delete');
    if (params) {
      rb.path('id', params.id, {});
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
   * delete an uploaded caff file
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `deleteFilesDelete$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteFilesDelete(params: {
    id: number;
    context?: HttpContext
  }
): Observable<void> {

    return this.deleteFilesDelete$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation postFilesCommentFileId
   */
  static readonly PostFilesCommentFileIdPath = '/files/comment/{fileId}';

  /**
   * create a new coment on a file
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postFilesCommentFileId()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postFilesCommentFileId$Response(params: {
    fileId: number;
    context?: HttpContext
    body?: CommentRequest
  }
): Observable<StrictHttpResponse<CommentResponse>> {

    const rb = new RequestBuilder(this.rootUrl, FilesService.PostFilesCommentFileIdPath, 'post');
    if (params) {
      rb.path('fileId', params.fileId, {});
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<CommentResponse>;
      })
    );
  }

  /**
   * create a new coment on a file
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `postFilesCommentFileId$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postFilesCommentFileId(params: {
    fileId: number;
    context?: HttpContext
    body?: CommentRequest
  }
): Observable<CommentResponse> {

    return this.postFilesCommentFileId$Response(params).pipe(
      map((r: StrictHttpResponse<CommentResponse>) => r.body as CommentResponse)
    );
  }

  /**
   * Path part for operation getFileDetails
   */
  static readonly GetFileDetailsPath = '/files/details/{id}';

  /**
   * Your GET endpoint.
   *
   * get the caff file details
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getFileDetails()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFileDetails$Response(params: {
    id: number;
    context?: HttpContext
  }
): Observable<StrictHttpResponse<CaffDetailsResponse>> {

    const rb = new RequestBuilder(this.rootUrl, FilesService.GetFileDetailsPath, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json',
      context: params?.context
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<CaffDetailsResponse>;
      })
    );
  }

  /**
   * Your GET endpoint.
   *
   * get the caff file details
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getFileDetails$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFileDetails(params: {
    id: number;
    context?: HttpContext
  }
): Observable<CaffDetailsResponse> {

    return this.getFileDetails$Response(params).pipe(
      map((r: StrictHttpResponse<CaffDetailsResponse>) => r.body as CaffDetailsResponse)
    );
  }

  /**
   * Path part for operation postFilesModifyId
   */
  static readonly PostFilesModifyIdPath = '/files/modify/{id}';

  /**
   * modify the details of a caff file
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postFilesModifyId()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postFilesModifyId$Response(params: {
    id: number;
    context?: HttpContext
    body?: ModifyCaffRequest
  }
): Observable<StrictHttpResponse<CaffDetailsResponse>> {

    const rb = new RequestBuilder(this.rootUrl, FilesService.PostFilesModifyIdPath, 'put');
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
        return r as StrictHttpResponse<CaffDetailsResponse>;
      })
    );
  }

  /**
   * modify the details of a caff file
   *
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `postFilesModifyId$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postFilesModifyId(params: {
    id: number;
    context?: HttpContext
    body?: ModifyCaffRequest
  }
): Observable<CaffDetailsResponse> {

    return this.postFilesModifyId$Response(params).pipe(
      map((r: StrictHttpResponse<CaffDetailsResponse>) => r.body as CaffDetailsResponse)
    );
  }

}
