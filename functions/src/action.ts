import * as functions from 'firebase-functions';
import Response from './response/response';

export default interface Action {
    (request: functions.Request): Promise<Response>;
}