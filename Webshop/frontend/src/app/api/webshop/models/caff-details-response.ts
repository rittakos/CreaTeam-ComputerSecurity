/* tslint:disable */
/* eslint-disable */
import { CommentResponse } from './comment-response';
import { PublicUserDetailsResponse } from './public-user-details-response';
export interface CaffDetailsResponse {
  comments: Array<CommentResponse>;
  creator: PublicUserDetailsResponse;
  duration: number;
  id: number;
  name: string;
  price: number;
  purchased: boolean;
  size: {
'width': number;
'height': number;
};
  tags: Array<string>;
  uploadDate: string;
}
