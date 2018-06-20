import * as functions from 'firebase-functions';

import * as generic from './tokenExtractor';

const extractToken = (request: functions.Request) => generic.extractToken(request, 'Bearer ');

export default extractToken;