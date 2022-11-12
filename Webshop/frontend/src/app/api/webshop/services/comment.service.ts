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

import { CommentRequest } from '../models/comment-request';
import { CommentResponse } from '../models/comment-response';

@Injectable({
  providedIn: 'root',
})
export class CommentService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
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

    const rb = new RequestBuilder(this.rootUrl, CommentService.PostFilesCommentFileIdPath, 'post');
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

}
