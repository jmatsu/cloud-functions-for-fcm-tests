import * as functions from 'firebase-functions';

export const create = (_: functions.Request) => {
    return { notification: { title: 'Portugal vs. Denmark', body: 'Great match!' } }
};