/* tslint:disable */
/* eslint-disable */
import { UserDetailsResponse } from './user-details-response';
export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  userDetails: UserDetailsResponse;
}
