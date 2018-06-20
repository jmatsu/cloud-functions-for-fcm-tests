import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

import { ok } from '../response/catalog';
import error, { ResponseError } from '../response/error';
import Response from '../response/response';

const credential = require("../../credential.json");

const config = {
  credential: admin.credential.cert(credential)
};

const messaging = admin.initializeApp(config, 'messaging app').messaging();

export const sendPush = (request: functions.Request, payload: admin.messaging.MessagingPayload): Promise<Response> => {
    return new Promise<string>((resolve, reject) => {
        const deviceToken = request.body.deviceToken;

        if (!!deviceToken) {
            console.log('deviceToken', deviceToken);
            resolve(deviceToken);
        } else {
            console.log('deviceToken is not found', deviceToken);
            reject(error.notFound('deviceToken is needed'));
        }
    }).then((deviceToken) => {
        return messaging.sendToDevice(deviceToken, payload)
            .then(response => {
                if (response.successCount > 0) {
                    console.log('Sent a message successfully', response);
                    return ok;
                } else if (response.failureCount > 0) {
                    console.log('Sent a message but failed', response);
                    throw error.badParameters(response);
                } else {
                    throw error.wrongServer('something wrong');
                }
            }
        )
    }).catch(e => {
        if (!(e instanceof ResponseError)) {
            console.log('Caught an error while sending a notification', e);
            return Promise.reject(error.wrongServer(e));
        } else {
            return Promise.reject(e);
        }
    });
};