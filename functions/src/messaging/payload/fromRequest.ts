import * as functions from 'firebase-functions';

export const create = (request: functions.Request) => {
    const data = request.body.data || {};
    const notification = request.body.notification;

    console.log('Data', data);
    console.log('Notification', notification);

    return !!notification ? { notification: notification, data: data } : { data: data };
};