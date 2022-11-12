/* tslint:disable */
/* eslint-disable */
import { PublicUserDetailsResponse } from './public-user-details-response';
export interface CommentResponse {
  comment: string;
  date: string;
  id: number;
  user: PublicUserDetailsResponse;
}
