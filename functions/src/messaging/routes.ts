import * as App from '../app';
import * as fromRequest from './payload/fromRequest';
import * as sampleData from './payload/sampleData';

import { sendPush } from './fcm';

const router = App.router();

// Declare endpoints
router.post('/messaging/fromRequest', (request) => sendPush(request, fromRequest.create(request)));
router.post('/messaging/sampleData', (request) => sendPush(request, sampleData.create(request)));

export {
    router
};