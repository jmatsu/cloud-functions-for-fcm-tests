import * as functions from 'firebase-functions';
import error from '../../response/error';

export const extractToken = (request: functions.Request, tokenPrefix: string) => {
    console.log('Check a token')

    const authHeader = request.headers.authorization;

    if (!authHeader || !authHeader.startsWith(tokenPrefix)) {
        console.error('Unauthorized', authHeader);
        return Promise.reject(error.unauthorizaed('header verification failed'));
    }

    const token = authHeader.substring(tokenPrefix.length);

    if (!token || token.length === 0) {
        console.error('Forbidden', token);
        return Promise.reject(error.forbidden('invalid token'));
    }
    
    return Promise.resolve(token);
};