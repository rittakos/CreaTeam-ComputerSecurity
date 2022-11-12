/* tslint:disable */
/* eslint-disable */
import { CommentResponse } from './comment-response';
import { PublicUserDetailsResponse } from './public-user-details-response';
export interface CaffDetailsResponse {
  comments: Array<CommentResponse>;
  creator: PublicUserDetailsResponse;
  description: string;
  id: number;
  name: string;
  price: number;
  size: {
'width': number;
'height': number;
};
  tags: Array<string>;
  uploadDate: string;
}
