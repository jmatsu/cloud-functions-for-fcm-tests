import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

import extractToken from '../token/bearerTokenExtractor';
import error from '../../response/error';

const auth = admin.initializeApp().auth();

export const authorize = (request: functions.Request) => {
    return extractToken(request)
        .then((token) => auth.verifyIdToken(token, true).catch((e) => {
            console.error('firebase authentication failed', e);
            return Promise.reject(error.forbidden('firebase authencation failed'));
        }));
};
