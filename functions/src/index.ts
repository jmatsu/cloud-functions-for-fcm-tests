import * as functions from 'firebase-functions';

import * as App from './app';
import * as messaging from './messaging/routes';

const app = App.app(true);

app.use(messaging.router);

export const api = functions.https.onRequest(app.responseHandler);
