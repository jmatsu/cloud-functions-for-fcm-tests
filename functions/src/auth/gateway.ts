import * as functions from 'firebase-functions';

export default interface AuthGateway {
    (request: functions.Request): Promise<any>;
}